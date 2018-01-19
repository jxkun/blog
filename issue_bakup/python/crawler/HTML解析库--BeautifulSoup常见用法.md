# BeautifulSoup常见用法
## 安装
Ubuntu
>$sudo apt-get install python-bs4

Python 的包管理器 pip
>pip install beautifulsoup4

[其他安装](http://www.baidu.com) 
其他处理HTML的解析库： lxml (解析HTML和XML，据说速度非常快)、 HTML parser (python自带解析库)

## 常用方法
### 1. 几个基础示例
示例1，常见异常处理：
```python
from urllib.request import urlopen 
from urllib.error import HTTPError 
from bs4 import BeautifulSoup 
def getTitle(url): 
    try:
        html = urlopen(url)  #服务器不存在或者网页在服务器上不存在抛出 HTTPError
    except HTTPError as e: 
        return None
    try:
        bsObj = BeautifulSoup(html.read()) 
        title = bsObj.body.h1  #从网页中提取的 <h1> 标签(不管多少层效果相同;例如:与bsObj.h1效果相同，当bsObj.body为None时,抛出AttributeError
    except AttributeError as e: 
        return None
    return title

title = getTitle("http://www.pythonscraping.com/pages/page1.html") 
if title == None: 
    print("Title could not be found")
else: 
    print(title)
```
示例2: find 与 findAll  
myTag为一个Tag对象示例,则有:
- myTag.get_text()  获取标签中的文本内容
- myTag.attrs 返回一个python字典对象,可以获取和操作标签属性
```python
from bs4 import BeautifulSoup
from urllib.request import urlopen
from urllib.request import HTTPError

html = urlopen("http://www.pythonscraping.com/pages/warandpeace.html")
bsObj = BeautifulSoup(html)
nameList = bsObj.findAll("span",{"class":{"green","red"}}) #选取class属性值为green 或者 red的span标签
for name in nameList:
	# print(name.get_text())  #get_text()去除标签只显示文本内容
	# print(name.attrs["class"]) # attrs 获取标签属性
	print(name) # 也会显示标签


''' find 与 findAll
findAll(tag, attributes, recursive, text, limit, keywords) 
find(tag, attributes, recursive, text, keywords)
几个参数的意思: 
tag 选取的标签,可以选取多个;
 attributes 属性对应属性值,为字典结构
recursive 布尔量,默认为True，表示可以抓取多层标签，为False时，只能抓取一级标签。
text表示用标签的文本内容取匹配，而不是标签属性。
limit 范围限制参数，表示输出前几条(注:获得的前几项结果是按照网页上的顺序排序的)
keyword 选择那些具有指定属性的标签。例如:
bsObj.findAll(id="text") 与  bsObj.findAll("", {"id":"text"}) 等效
'''
```
### 2. 其他BeautifulSoup常用对象
- BeautifulSoup对象  
前面代码示例中的 bsObj
- 标签 Tag 对象  
BeautifulSoup 对象通过 find 和 findAll，或者直接调用子标签获取的一列对象或单个对象
- NavigableString 对象  
用来表示标签里的文字，不是标签（有些函数可以操作和生成 NavigableString 对象， 而不是标签对象）。
- Comment 对象  
用来查找HTML文档的注释标签，\<!-- 像这样 -->

### 3.  导航树(Navigating Trees)
findAll 函数通过标签的名称和属性来查找标签。  
而导航树（Navigating Trees）则是通过标签在文档中的位置来查找标签。
例如：
```javascript
html 
	— body 
	— div.wrapper 
		— h1 
		— div.content 
		— table#giftList 
			— tr
				— th 
				— th
			— tr.gift#gift1 
				— td 
				— td 
					— span.excitingNote
	...
```
#### 标签间的关系
1. 子标签与后代标签
- BeautifulSoup 库里，孩子（child）和后代（descendant）有显著的不同，子标签就是一个父标签的下一级，而后代标签是指一个父标签下面所有级别的标签。
- 一般情况下，BeautifulSoup 函数总是处理当前标签的后代标签。找出后代标签 ***.descendants*** 例如，  
bsObj.body.h1 选 择了 body 标签后代里的第一个 h1 标签，不会去找 body 外面的标签。
- 若只想找出子标签，可以用 ***.children*** 标签。例如：  
bsObj.find("table",{"id":"giftList"}).children

2. 处理兄弟标签
对象不能把自己作为兄弟标签。任何时候你获取一个标签的兄弟标签，都不会包含这个标签本身。
- next_siblings  取标签后的全部兄弟标签
- next_sibling 取标签后的一个兄弟标签
- previous_siblings
- previous_sibling
```python
from urllib.request import urlopen 
from bs4 import BeautifulSoup

html = urlopen("http://www.pythonscraping.com/pages/page3.html") 
bsObj = BeautifulSoup(html)
for sibling in bsObj.find("table",{"id":"giftList"}).tr.next_siblings: 
	print(sibling)
```
3. 父标签处理
使用BeautifulSoup 的父标签查找函数，有 parent 和 parents 。
```python
print(bsObj.find("img",{"src":"../img/gifts/img1.jpg" }).parent.previous_sibling.get_text())
```
### 4. 正则表达式
正则表达式常用符号

符号 |  含 义 | 例子 | 匹配结果
-----|--------|-------|------------
\* 	 |匹配前面的字符、子表达式或括号里的字符 0 次 或多次| a*b* | aaaaaaaa，aaabbbbb， bbbbbb
\+	 |匹配前面的字符、子表达式或括号里的字符至少 1 次|a+b+ |aaaaaaab，aaabbbbb， abbbbbb
[]   |匹配任意一个字符（相当于“任选一个”）| [A-Z]* | APPLE，CAPITALS， QWERTY
()	 |表达式编组（在正则表达式的规则里编组会优先 运行）| (a*b)* | a a a b a a b，a b a a a b， ababaaaaab
{m,n} |匹配前面的字符、子表达式或括号里的字符m到n次（包含m或 n）| a{2,3}b{2,3} |aabbb，aaabbb，aabb
[^]		|匹配任意一个不在中括号里的字符 | [^A-Z]* |  app l e，l owe r ca s e， qwerty
\| 		|匹配任意一个由竖线分割的字符、子表达式（注 意是竖线，不是大字字母 I）|b(a\|i\|e)d |bad，bid，bed
.	|匹配任意单个字符（包括符号、数字和空格等）| b.d | bad，bzd，b$d，b d
^   |指字符串开始位置的字符或子表达式 | ^a | apple，asdf，a
\   |转义字符（把有特殊含义的字符转换成字面形式） |  \\.  | .
$   |经常用在正则表达式的末尾，表示“从字符串的末端匹配”。如果不用它，每个正则表达式实际都带着“.*”模式，只会从字符串开头进行匹配。这 个符号可以看成是 ^ 符号的反义词 |[A-Z]*[a-z]*$ | ABCabc，zzzyx，Bob
?!  |不包含”。这个奇怪的组合通常放在字符或正则表达式前面，表示字符不能出现在目标字符串里。 如果要在整个字符串中全部排除某个 字符，就加上 ^ 和 $ 符号 | ^((?![A-Z]).)*$| no-caps-here，$ymb0ls a4e f!ne

#### BeautifulSoup 和正则表达式
BeautifulSoup 和正则表达式总是配合使用的,大多数支 持字符串参数的函数（比如，find(id="aTagIdHere")）都可以用正则表达式实现。例如: 抓取商品图片 \<img src="../img/gifts/img3.jpg">
```python
from urllib.request import urlopen
from bs4 import BeautifulSoup 
import re
html = urlopen("http://www.pythonscraping.com/pages/page3.html") 
bsObj = BeautifulSoup(html)
images = bsObj.findAll("img",{"src":re.compile("\.\.\/img\/gifts/img.*\.jpg")}) 
for image in images: 
	print(image["src"])
---------输出:
../img/gifts/img1.jpg
../img/gifts/img2.jpg
../img/gifts/img3.jpg
../img/gifts/img4.jpg
../img/gifts/img6.jpg
```
#### Lambda表达式达到正则效果
BeautifulSoup 允许把特定函数类型当作 findAll 函数的参数。唯一的限制条件是这些 函数必须把一个标签作为参数且返回结果是布尔类型。BeautifulSoup 用这个函数来评估它 遇到的每个标签对象，最后把评估结果为“真”的标签保留，把其他标签剔除。  
例如：面的代码就是获取有两个属性的标签：
```python
soup.findAll(lambda tag: len(tag.attrs) == 2)
---------这行代码会找出下面的标签：
<div class="body" id="content"></div> 
<span style="color:red" class="title"></span>
```








