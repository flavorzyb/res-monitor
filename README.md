# res-monitor
Automatically exported from code.google.com/p/res-monitor
用于监控文件变化，并自动同步到svn仓库中以便于管理.

本项目使用maven进行编译和构建，请自行安装maven，maven帮助请点击这里

一、服务目标

实时监控目标目录的文件变化，将有更新的文件实时的同步到svn仓库中，以便于后续的管理和维护
二、服务处理流程

启动程序-->监控目录文件变化-->同步变更的文件

启动程序-->每15分钟自动全量同步

说明：

该服务启动之后，每15分钟自动全量同步目标目录，目的是防止异常情况下导致某些文件可能会导致同步失败而错失同步机会。
三、服务部署

每启动一次该服务，该服务会自动检测该服务是否已有正在运行的实例，如果该服务已运行则该程序自动结束，

以保证一台监控机上只有一个运行的服务实例；如果该服务不存在或没运行，则启动此服务。
四、目录结构

|---configs

|---libs

|---logs

|---resource-monitor-1.0-SNAPSHOT.jar

|---starResourceMonitorServer.bat

说明:

configs: 配置文件目录

libs: 所使用的lib库

logs: 日志目录

resource-monitor-1.0-SNAPSHOT.jar:监控主程序

starResourceMonitorServer.bat: windows下的启动服务脚本
