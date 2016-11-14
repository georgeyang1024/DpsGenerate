# DpsGenerate
安卓最佳屏幕适配解决方案

# 使用方法:
1. android Stdio-File-New-new module,选择本项目目录
2. 修改DpTransUtil.java 中的适配参数

```
参数说明:

生成常用dp值
generate(路径, UI设计图的宽度, 最小兼容的屏幕宽度DP值, 最大兼容的屏幕宽度DP值, 最小到最大DP的增量)
generate(路径, UI设计图的宽度, 指定兼容的DP值)

增加指定dP值(默认1%-100%，如果不满足需要可以用该方法)
appendPercent(路径, 指定百分比)

增加指定的X宽度对应dp值(默认1-Design宽度,如果不满足需要可以用该方法)
appendXValue(path, 指定X值)


```

3. 开始允许
两种方法可以运行，1：传统的javac方法执行DpTransUtil，2:进入DpsGenerate目录，执行gradle runJava

