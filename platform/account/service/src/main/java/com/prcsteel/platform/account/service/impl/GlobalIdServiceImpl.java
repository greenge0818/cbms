package com.prcsteel.platform.account.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.account.model.dto.GlobalIdModifier;
import com.prcsteel.platform.account.model.enums.GlobalIdModule;
import com.prcsteel.platform.account.model.model.GlobalId;
import com.prcsteel.platform.account.persist.dao.GlobalIdDao;
import com.prcsteel.platform.account.service.GlobalIdService;

/**
 * 全局ID服务
 * @author zhoukun
 */
@Service("globalIdService")
public class GlobalIdServiceImpl implements GlobalIdService {

	private final static Logger logger = LoggerFactory.getLogger(GlobalIdServiceImpl.class);
	
	@Resource
	private GlobalIdDao globalIdDao;
	
	private Timer refreshMaxIdTimer;
	
	private Map<GlobalIdModule, IdModule> modules;
	
	private final static int CHECK_ID_CAPACITY_PERIOD = 2000;// 检查ID可用量的时间周期
	
	private final static int UPDATE_ID_THRESHOLD = 100;// 剩余ID不足阀值时，开始更新ID可使用区间
	
	private final static int ID_CAPACITY = 1000000;// 一次刷新占用多少个ID区间
	
	@PostConstruct
	public void init(){
		refreshMaxIdTimer = new Timer(true);
		refreshMaxIdTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				checkAllModule();
			}
		}, CHECK_ID_CAPACITY_PERIOD, CHECK_ID_CAPACITY_PERIOD);
	}
	
	private void checkAllModule(){
		if(modules == null){
			initModules();
		}
		
		for (Entry<GlobalIdModule, IdModule> kv : modules.entrySet()) {
			IdModule module = kv.getValue();
			if(module.getCurrentIdValue().get() + UPDATE_ID_THRESHOLD >= module.getMaxIdValue().get()){
				occupyIdRange(module);
			}
		}
		synchronized (this) {
			this.notifyAll();
		}
		
	}

	private void occupyIdRange(IdModule module) {
		// 占用新的ID区间
		String moduleName = module.getModule().toString();
		int occupySuccess = 0;
		long beginValue = 0;
		long maxValue = 0;
		while(occupySuccess != 1){
			GlobalId newIdInfo = globalIdDao.selectByModuleName(moduleName);
			beginValue = newIdInfo.getCurrentId() + 1;
			maxValue = newIdInfo.getCurrentId() + ID_CAPACITY;
			GlobalIdModifier gm = new GlobalIdModifier(moduleName, newIdInfo.getCurrentId(), maxValue);
			occupySuccess = globalIdDao.setCurrentId(gm);
		}
		module.getBeginIdValue().set(beginValue);
		module.getCurrentIdValue().set(beginValue);
		module.getMaxIdValue().set(maxValue);
		logger.info("Occupy new range,{}:[{},{}]",moduleName,beginValue,maxValue);
	}

	private void initModules() {
		modules = new HashMap<>();
		List<GlobalId> allModules = globalIdDao.selectAll();
		if(allModules != null && allModules.size() > 0){
			for (GlobalId m : allModules) {
				IdModule id = new IdModule();
				id.setModule(GlobalIdModule.valueOf(m.getModuleName()));
				id.getBeginIdValue().set(m.getCurrentId() + 1);
				id.getCurrentIdValue().set(id.getBeginIdValue().get());
				modules.put(id.getModule(), id);
			}
		}
	}
	
	@Override
	public Long getId(GlobalIdModule module) {
		if(modules == null){
			waitInitOrNewIdAvailable();
		}
		IdModule im = modules.get(module);
		Long id = im.getCurrentIdValue().incrementAndGet();
		for(; id >= im.getMaxIdValue().get(); id = im.getCurrentIdValue().incrementAndGet()){
			waitInitOrNewIdAvailable();
		}
		return id;
	}
	
	private void waitInitOrNewIdAvailable(){
		try {
			logger.info("No id avaiable,wait....");
			synchronized (this) {
				this.wait();
			}
			logger.info("New id avaiable,continue.");
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	private class IdModule{
		
		private GlobalIdModule module;
		
		private AtomicLong currentIdValue = new AtomicLong();
		
		private AtomicLong maxIdValue = new AtomicLong();
		
		private AtomicLong beginIdValue = new AtomicLong();

		public GlobalIdModule getModule() {
			return module;
		}

		public void setModule(GlobalIdModule module) {
			this.module = module;
		}

		public AtomicLong getCurrentIdValue() {
			return currentIdValue;
		}

		public AtomicLong getMaxIdValue() {
			return maxIdValue;
		}

		public AtomicLong getBeginIdValue() {
			return beginIdValue;
		}
		
	}
}
