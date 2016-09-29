规范说明

数据库结构更改需要同步E-R图设计文件和SQL脚本文件，E-R图更改前请先从Git上获取最新版本。
注：E-R图文件为项目根目录下的“doc/design/cbms.mwd”文件。

文件结构说明如下：
环境/版本/脚本文件夹/脚本文件
例： dev/1.0-SNAPSHOT/db_int/XXXX.sql,代表开发环境1.0-SNAPSHOT版数据初始化脚本。
    dev/1.0-SNAPSHOT/upgrade/XXXX.sql,代表开发环境1.0-SNAPSHOT版数据库升级脚本。
    dev/1.0-SNAPSHOT/upgrade/XXXX_20150731.sql,代表2015年07月31日创建的开发环境1.0-SNAPSHOT版数据库升级脚本。

1. 目录及文件说明：
dev     开发环境
local   本地环境
pro     正式环境
sim     仿真环境
test    测试环境

注：每个环境下包含一个当前版本的目录，如：“1.0-SNAPSHOT”。
    如果脚本比较多要建不同目录对脚本进行分类。
db_init 数据库初始脚本
upgrade 数据库升级脚本

xxx_20150731.sql 表示2015年07月31日创建的脚本

2. SQL脚本存放说明
如果添加脚本，每个环境目录下都要复制一份，除非是此脚本仅需要在特定环境下执行。

3.SQL脚本文件命名
脚本文件命名应包含执行序号，执行目的，创建日期。序号是为了保证脚本执行的先后顺序。
格式如下：序号_目的_日期.sql
示例：
1_create_cbms_schema.sql   代表创建cbms数据库，并且最先执行。
2_insert_user_20150731.sql 代表2015年07月31日创建的创建用户数据的脚本，执行在1_create_cbms_schema.sql之后。



