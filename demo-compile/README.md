## 编译

本模块用来学习编译原理知识  
实现了一个简单的解释性编程语言  
### 语法规则
#### 关键字
var、func、if、else、while、return
#### 内置函数
print()
#### 语法
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
if (1 == 1) {
    return 1 + 1;
}
```
while语句
```
while (1 == 1) {
    return 2;
}
```
### 演示地址
[地址](https://demo.wuareb.top/lang.html)