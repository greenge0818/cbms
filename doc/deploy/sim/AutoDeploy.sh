#!/bin/bash
##Author: Rolyer Luo
##Date: 26/08/2015
##version: 2.1
##Auto Deploy Script

##############################################################
##
## Deploy Steps:
## 1. shutdown tomcat ...................................DONE;
## 2. backup database ...................................DONE;
## 3. execute upgrade sql ...............................DONE;
## 4. backup upgrade sql ................................DONE;
## 5. backup war file ...................................DONE;
## 6. clean up tomcat ...................................DONE;
## 7. copy the latest WAR file to tomcat ................DONE;
## 8. start tomcat ......................................DONE;
## 9. check status ......................................DONE;
## 10. verify the result ................................TODO;
##
##
## Roll Back Steps:......................................DONE;
## 1. shutdown tomcat ...................................DONE;
## 2. roll back database ................................TODO;
## 3. clean up tomcat ...................................DONE;
## 4. copy the latest WAR file to tomcat ................DONE;
## 5. start tomcat ......................................DONE;
## 6. check status ......................................DONE;
## 7. verify the result .................................TODO;
##
##############################################################

##############################################################
#定义变量，依据修改以下配置。
##############################################################
DEFAULT_HOSTS="10.117.106.214"

USERNAME="root"
PASSWORD="Prcsteeljsb11"
#INSTANCE="tomcat"
TOMCAT_HOME="/opt/tomcat8-cbms"
BAK_DIR="/opt/backup/cbms"
TARGET_HOME="/tmp/cbms"

#SQL文件目录
SQL_FILE_DIR="/tmp/cbms_db_update"
SQL_BACK_DIR="/opt/cbms/db_update"

#数据库配置信息
DB_SERVER="rdssqmhhvd63a16qpe5j.mysql.rds.aliyuncs.com"
DB_SCHEMA="steel_cbms"
DB_USERNAME="steel_cbms"
DB_PASSWORD="prcsteelcbms"
DB_BACKUP_DIR="/opt/backup/database"
##############################################################

#以下参数无需修改
TOMCAT_PID=""
HOST=""

function waiting() {
	b=''
	for ((i=0;i<=100;i+=2))
		do
			printf "\e[00;32mProcessing:[%-50s]%d%%\r\e[00m" $b $i
			sleep 0.2
			b=.$b
		done
	echo
}

function execute_log(){
    cmd=$1;
    echo ""
    echo "EXECUTE $(date "+%Y/%m/%d %H:%M:%S") [${cmd}]"
    echo ""
}

function db_dump(){
    echo "Start backup database"
#    SERVER=$1

    execute_log "mysqldump -h ${DB_SERVER} -u ${DB_USERNAME} -p${DB_PASSWORD} ${DB_SCHEMA} -R > ${DB_BACKUP_DIR}/aout_backup_${DB_SCHEMA}_$(date "+%Y%m%d_%H%M%S").sql"
    mysqldump -h ${DB_SERVER} -u ${DB_USERNAME} -p${DB_PASSWORD} -R ${DB_SCHEMA} | gzip > ${DB_BACKUP_DIR}/aout_backup_${DB_SCHEMA}_$(date "+%Y%m%d_%H%M%S").sql.gz
}

function db_upgrade() {
    files=`ls ${SQL_FILE_DIR}`
    if [ -z "${files}" ]; then
        echo "No upgrade for database"
    else
        echo "Start to update the database"
        for file in `ls ${SQL_FILE_DIR}/*.sql |sort`
        do
            execute_log "mysql -h ${HOST} -u ${DB_USERNAME} -p${DB_PASSWORD} steel_cbms < ${file}"
            mysql -h ${HOST} -u ${DB_USERNAME} -p${DB_PASSWORD} steel_cbms < ${file}
        done

        sql_back_dir="${SQL_BACK_DIR}/SQL_$(date "+%Y%m%d_%H%M%S")"

        execute_log "mkdir ${sql_back_dir}"
        mkdir ${sql_back_dir}

        execute_log "mv ${SQL_FILE_DIR}/* ${sql_back_dir}"
        mv ${SQL_FILE_DIR}/* ${sql_back_dir}
    fi
}

function shutdown_tomcat(){
    TOMCAT_PID=$(ssh ${DESTINATION} ps -ef |grep ${TOMCAT_HOME} |grep -v 'grep'|awk '{print $2}')
    echo "Find tomcat PID ${TOMCAT_PID} on host ${HOST}"

    if [ -n "${TOMCAT_PID}" ]; then
        echo "Start shutdown tomcat [${TOMCAT_HOME}]"

        ssh ${DESTINATION} ${TOMCAT_HOME}/bin/shutdown.sh
        waiting

        pid=$(ssh ${DESTINATION} ps -ef |grep ${TOMCAT_HOME} |grep -v 'grep'|awk '{print $2}')
        if [ -n "$pid" ]; then
            echo "Killed Tomcat"

            ssh ${DESTINATION} kill ${pid}
            waiting
        fi
    fi
}

function cleanup(){
    war=$(ssh ${DESTINATION} ls -rt ${TOMCAT_HOME}/webapps/web.war | tail -1)
    if [ war!="" ]; then
        echo "Start backup WAR package"
        execute_log "ssh ${DESTINATION} mv ${TOMCAT_HOME}/webapps/web.war ${BAK_DIR}/web_$(date "+%Y%m%d_%H%M%S").war"
        ssh ${DESTINATION} mv ${TOMCAT_HOME}/webapps/web.war ${BAK_DIR}/web_$(date "+%Y%m%d_%H%M%S").war
    fi
    echo "Start clean up tomcat work directory";
    execute_log "ssh ${DESTINATION} rm -rf ${TOMCAT_HOME}/webapps/web"

    ssh ${DESTINATION} rm -rf ${TOMCAT_HOME}/webapps/web

    echo "Start deploying the new WAR package"
    execute_log "ssh ${DESTINATION} mv ${TARGET_HOME}/prcsteel-platform-order-web-*.war ${TOMCAT_HOME}/webapps/web.war"
    ssh ${DESTINATION} mv ${TARGET_HOME}/prcsteel-platform-order-web-*.war ${TOMCAT_HOME}/webapps/web.war
}

function restore(){
    war=$(ssh ${DESTINATION} ls -rt ${TOMCAT_HOME}/webapps/web.war | tail -1)
    if [ war!="" ]; then
        echo "Start clean up tomcat work directory"
        execute_log "ssh ${DESTINATION} rm ${TOMCAT_HOME}/webapps/web.war"

        ssh ${DESTINATION} rm ${TOMCAT_HOME}/webapps/web.war
    fi
    execute_log "ssh ${DESTINATION} rm -rf ${TOMCAT_HOME}/webapps/web"
    ssh ${DESTINATION} rm -rf ${TOMCAT_HOME}/webapps/web

    backwar=$(ssh ${DESTINATION} ls -rt ${BAK_DIR}/**.war | tail -1)
    if [ backwar!="" ]; then
        echo "Being restored from backup"
        execute_log "ssh ${DESTINATION} cp ${backwar} ${TOMCAT_HOME}/webapps/web.war"

        ssh ${DESTINATION} cp ${backwar} ${TOMCAT_HOME}/webapps/web.war
    fi
}

function startup_tomcat(){
    echo "Restarting Tomcat"
    ssh ${DESTINATION} ${TOMCAT_HOME}/bin/startup.sh
    waiting
}

function check_status(){
    pid=$(ssh ${DESTINATION} ps -ef |grep ${TOMCAT_HOME} |grep -v 'grep'|awk '{print $2}')
    if [ -n "$pid" ]; then
        echo "Find tomcat PID ${pid} on host ${HOST}"
        echo  -e "\e[00;32mHost[${HOST}] has been successfully deployed\e[00m"
    else
        echo  -e "\e[00;31mHost[${HOST}] failed to deploy\e[00m"
    fi
}

function deploy(){
    action=$1;
    if [ "${action}" == "deploy" ]; then
        # backup .war file and clean tomcat
        cleanup;
    elif [ "${action}" == "rollback" ]; then
        # restore tomcat
        restore;
    else
        echo "Invalid action paramater: ${action}"
    fi

    # deploy latest war file
    startup_tomcat

    # check status
    check_status
}

function process(){

    if [ "${action}" == "deploy" ]; then
        echo "DEPLOY"
    fi
    if [ "${action}" == "rollback" ]; then
        echo "ROLLBACK"
    fi

    ## git host list
    OLD_IFS="$IFS";
    IFS=","
    if [ "$2" == "" ]; then
        hosts=(${DEFAULT_HOSTS})
    else
        hosts=($2)
    fi
    IFS="$OLD_IFS"

    ## shutdown all the server
    echo "Shutdown all the server"
    for host in ${hosts[@]}; do
        HOST=${host}
        DESTINATION=${USERNAME}@${host}

        #shutdown tomcat if it already running
        shutdown_tomcat
    done

    if [ "${action}" == "deploy" ]; then
        # backup database
        db_dump

        # upgrade database
        db_upgrade
    fi

    ## deploy
    for host in ${hosts[@]}; do
        HOST=${host}
        DESTINATION=${USERNAME}@${host}

        echo "###############################################################"
        echo "Host: ${HOST}"
        echo "###############################################################"

        if [ "${action}" == "deploy" ]; then
            deploy "deploy"
        elif [ "${action}" == "rollback" ]; then
            deploy "rollback"
        else
            echo "Invalid action paramater: ${action}"
        fi
    done
}


action=$1
hosts=$2

process ${action} ${hosts}
