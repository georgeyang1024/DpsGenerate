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

3. 开始执行

两种方法可以运行

```
1:传统的javac方法执行DpTransUtil
2:进入DpsGenerate目录，执行gradle runJava
```

#使用适配
以UI效果图720*1280为例，实际上，高度是可以忽略的，因为像素点是正方形的，有些手机720宽度，但不是1280高度

布局宽高安装ui标注写@dimen/x??

如果按照一定宽高比例，布局写@dimen/p?? ,p100代表沾满屏幕宽度

文字大小使用@dimen/f?? f??的实际单位是sp，手机字体是默认的普通大小时和dp没差别，但手机字体改成偏大或偏小，就起效果了

#联系我

georgeyang1024@gmail.com


