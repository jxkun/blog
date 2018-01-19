# pandas数据读写
## I/O API函数  
pandas库提供的一组I/O API函数，这些函数完全对称分为两大类，***读取函数*** 和 ***写入函数***

读取函数 | 写入函数
------------|-----------
read_csv    | to_csv
read_excel | to_excel
read_hdf   | to_hdf
read_sql    | to_sql
read_json  | to_json
read_html | to_html
read_stata | to_stata
read_clipboard | to_clipboard
read_pickle | to_pickle
read_msgpack | to_msgpack
read_gbq    | to_gbq

## 读取文件
### 1. csv 和 文本文件
csv格式文本：文件的每一行的多个元素都是用逗号隔开。
- 读取csv格式数据。使用 read_csv 或 read_table 函数。
>rea_csv的sep默认为逗号，所以不需要指定，sep支持正则。
read_csv 函数的的几个选项：
header 选项，若没有表头，则将header置为None，pandas会为其添加默认表头。
names 选项，指定表头，直接将各列名称的数组赋值给它即可。
index_col选项， 创建一个具有等级的DataFrame对象，将所有想转换为索引的列名赋值给index_col
skiprows 选项，排除多余行，skiprows = 5是指排除前五行，skiprows = [1,2]是指排除第1和第2行。
nrows 选项，是指从起始行开始往后读多少行。
chunksize 选项，指每隔几行取一行

>read_table函数读取CSV文件，需要用sep选项指定分割符 ","
read_table函数中的选项包含上面的csv的选项，且功能一样。

部分正则 | 作用
---------|-------
.            | 换行符以外的单字符
\d         | 数字
\D         | 非数字字符
\s          | 空白字符
\S          | 非空白字符
\n          | 换行符
\t           | 制表符
\uxxxx    | 十六进制数字xxxx表示的unicode字符


```python
'''
myCSV_01.csv文件中内容
white,red,blue,green,animal
1,5,2,3,cat
2,7,8,5,dog
2,2,8,3,duck
3,3,6,7,horse
'''
import pandas as pd
csvframe1 = pd.read_csv('myCSV_01.csv')
csv_table = pd.read_table('myCSV_01.csv',sep=',')  # 效果与第一个等同

csvframe2 = pd.read_csv('myCSV_01.csv',header = None)
csvframe3 = pd.read_csv('myCSV_01.csv',names=('white','red','blue','green','animal')) # 指定表头
csvframe4 = pd.read_csv('myCSV_01.csv', index_col=['white','red']) # 将white 和 red 两列作为2级索引
```
- 写入文本文件
使用 to_csv() 函数写入，参数为即将生成的文件名。
>to_csv(fname)
常用选项有：
index选项，默认为True，写入数据时连同索引一起写入，置为False，则不写入索引。
header选项，默认为True,写入数据时连同表头一起写入，置为False，则比写入表头。
na_rep 选项，把空字段替换为你需要的值，常用有 NULL、0、NaN
```python
frame = pd.DataFrame(np.arange(16).reshape(4,4))
frame.to_csv('frame.csv',index=False, header=False, na_rep= 'NaN')
```

### 2. 读写HTML 文件
- 从 html 文件中读取数据
read_html()函数可以解析HTML页面，找到HTML表格，若找到就将其转换为DataFrame对象,返回DataFrame对象列表。
```python
html_frame = pd.read_html('myFrame.html') # 读取html文件
rank = pd.read_html('https://cn.bing.com/')  # 读取url中的html表格，若没有表格会抛出ValueError异常
```
- 写入数据到HTML文件。
to_html() 函数，将DataFrom对象转换成table结构的html表格
```python
frame = pd.DataFrame(np.arange(4).reshape(2,2))
print( frame.to_html() ) 
----------输出：
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>0</th>
      <th>1</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>0</td>
      <td>1</td>
    </tr>
    <tr>
      <th>1</th>
      <td>2</td>
      <td>3</td>
    </tr>
  </tbody>
</table>
```
### 3. 读写XML 文件  
pandas中没有专门处理XML格式的函数。python中的Ixml库对于处理大文件方面性能优异，可以将它与pandas整合起来来处理xml。通过lxml将xml中的数据取出然后放入到DataFrame中。

### 4. 读写Microsoft Excel 文件
read_excel() 函数能够读取Excel2003（.xls）和Excel2007（.xlsx）两种类型的文件。该函数之所以能够读取Excel，是因为它整合了xlrd模块。
```python
frame = pd.read_excel('data.xlsx') # 默认读取文件中的第一个工作表
# 读取第二张工作表需要在第二个参数指定工作表名称，或者序号（索引）
frame2 = pd.read_excel('data.xlsx','sheet2') 

frame.to_excel('data2.xlsx') # 写入到data2.xlsx表中，将DataFrame转换为Excel
```

## JSON 、HDF5 格式数据
### json数据
JSON(JavaScript Object Notation, JavaScript对象标记) ,json中的字符串需要使用双引号，用单引号可能会出现异常。

```python
# 多级索引的DataFrome转成json可能会出现异常。
mframe = pd.DataFrame(np.random.rand(16).reshape(4,4),
                      index=['white1','white2','red1','red2'] ,
                      columns=['pen1','pen2','paper1','paper2'])
mframe.to_json('json.json')  # 写入json

# 读取列表格式的 json
pd.read_json('json.json')

# 读取文件结构的json,文件为book.json例如：
'''
[{"write":"ross",
  "nationality":"usa",
  "book":[
    {"title":"xml","price":23}
  ]
}, {"write":"bob",
  "nationality":"UK",
  "book":[
    {"title":"python","price":35}
  ]
}]
'''
# 将字典结构的文件转换为列表形式，规范化
from pandas.io.json import json_normalize
file = open("book.json",'r')
text = file.read()
text = json.loads(text)
frame = json_normalize(text,'book') #会读取所有以book为键的元素的值，元素所有属性转换为嵌套的列名称。
frame2 = json_normalize(text,'book',['write','nationality']) # 增加和book位于同一级的其他键的值，将键名的列表作为第三个参数传入。
print(frame)
print(frame2)
-------------输出
   price   title
0     23     xml
1     35  python
   price   title write nationality
0     23     xml  ross         usa
1     35  python   bob          UK
```
### HDF5格式
python提供两种操作HDF5格式数据的方法：PyTables 和 h5py。  
pandas提供HDFStore类，类似于dict类，它用于PyTables存储pandas对象。使用HDF5格式前需要导入HDFStore类:
>from pandas.io.pytables import HDFStore

```python
store = HDFStore('store.h5') # 创建一个用来存储DataFrame数据的文件
store['obj1'] = mframe  # 将frame存储到store.h5文件中
frame = store['obj1'] #取出store.h5中存储的Dataframe数据，返回DataFrame对象。
```
## 序列化
pickle模块（或cpickle，据说cpickle使用c语言实现的，比pickle更快）默认使用ASCII表达式，可以增强可读性。序列化 dumps() 反序列化 loads() 。  
pandas实现序列化：  
```python
mframe.to_pickle('frame.pkl') # 序列化
pd.read_pickle('frame.pkl') # 反序列化
```

## 数据库数据
pandas.io.sql模块提供独立于数据库，叫做sqlalchemy的同一接口。该接口简化了连接模式，连接数据库只需要使用create_engine()函数，可以用它配置驱动器所需的用户名、密码、端口和数据库实例等所有属性。各种数据库的连接方法：
```python
from sqlalchemy import  create_engine
#连接postgresql
engine = create_engine('postgresql://scott:tiger@localhost:5432/mydatabase')
# 连接 mysql
engine = create_engine('mysql+mysqldb://scott:tiger@localhost/foo')
#连接 oracle
engine = create_engine('oracle://scott:tiger@127.0.0.1:1521/sidname')
#连接SQLite
engine = create_engine('sqlite:///foo.db')
# 连接MSSQL
engine = create_engine('mssql+pyodbc://mydsn')
#连接SQL Server
engine = create_engine('mssql+pyodbc://mydsn')
```
1. sqlite3数据读写
```python
import numpy as np
import pandas as pd
from sqlalchemy import  create_engine

mframe = pd.DataFrame(np.random.rand(16).reshape(4,4),
                      index=['white1','white2','red1','red2'] ,
                      columns=['pen1','pen2','paper1','paper2'])
engine = create_engine('sqlite:///foo.db')  # 连接数据库
mframe.to_sql('colors',engine)  # 把DataFrame转换成数据库表
a = pd.read_sql('colors',engine) # 读取数据库，参数为表名和engine实例

# 其他数据库读写类似
```

  
  
  
  
  
  