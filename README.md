# PackWar
#### Tool for MyEclipse JavaWeb Project update based on incremental changed files
---
#### 功能：
1. 不校验模式
    + 获取项目文件列表，手动勾选文件进行打包
    + 上传升级包至SVN
2. 本地SVN模式
    + 获取项目文件列表，自动勾选新增、修改文件
    + 显示修改文件与原文件差异内容
    + 打包
    + 上传升级包至SVN
3. 远程SVN模式
    + 根据提交时间获取SVN代码提交记录
    + 支持多人协作模式，可按提交者选择提交代码
    + 获取项目文件列表，自动勾选选择的代码文件
    + 打包
    + 上传升级包至SVN

另：工具支持Swing界面（PackMain），具备1-2模式的简版打包功能。
