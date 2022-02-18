## html网页中java语法高亮

#### 原理
读取java文件，解析成一个个token，根据不同类型生成html代码  
例如：如果发现token是关键字时，会生成`<span style="color: #CC7832;">public</span>`这种html代码  

#### 运行方法
[test用例](https://github.com/wuare/demo/blob/master/demo-highlight/src/test/java/top/wuareb/highlight/TestLexer.java#L37)

#### 演示地址
[网址](https://demo.wuareb.top/index.html)

#### 效果图
![](https://github.com/wuare/demo/blob/master/demo-highlight/images/image_01.png)