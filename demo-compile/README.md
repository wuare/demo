## 编译

本模块用来学习编译原理知识  
实现了一个简单的解释性编程语言  
### 语法规则
#### 关键字
var、func、if、else、while、for、foreach、break、return、true、false、nil
### 类型
* `字符串` 由英文双引号引起来的文本，如："abc"
* `数字` 数字类型，如：123
* `数组` 由中括号括起来的若干元素组成，如：[1, 2, 3]
* `true` 布尔类型，表示真
* `false` 布尔类型，表示假
* `nil` 空类型
#### 内置函数
* `print(...)` 打印函数，参数个数不固定
* `time()` 获取当前毫秒数
* `len(?)` 获取数组或字符串长度方法，参数为数组或字符串
* `arrAdd(?, ?)` 数组添加元素函数，第一个参数为数组，第二个参数为添加的元素
* `arrNew(?)` 创建数组，参数为指定数组长度
* `strAt(?, ?)` 获取字符串的某个字符，第一个参数为字符串，第二个参数为字符的位置，数字类型
#### 语法
注释
```
// 这是注释，只支持单行注释
```
变量声明
```
var a = 1;
```
函数声明
```
func a0(b, c) {
    return b + c;
}
```
函数调用
```
a0(1, 1);
```
if语句
```
if (2 > 1) {
    return 1 + 1;
}
```
while语句
```
while (true) {
    return 2;
}
```
for语句
```
for (var i = 0; i < 2; i = i + 1) {
    // do something
}
```
foreach语句
```
foreach (x in 3) {
    // do something
}
```
### 演示地址
[地址](https://demo.wuareb.top/lang.html)