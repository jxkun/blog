# pandas库学习笔记

## 搭建环境
### 安装
- 用Anaconda安装  
  condas install pandas

- PyPI安装  
pip install pandas
- ubuntu linux系统安装  
sudo apt-get intall python-pandas
- 源代码安装(没试过)  
git clone git://github.com/pydata/pandas.git  
cd pandas  
python setup.py install

### 测试pandas是否安装成功  
nose模块扩展了unittest模块功能，Python的unittest是用来测试代码的，nose模块与之相比简化了测试代码。  
先确保Python安装有nose模块，运行nosetests pandas 测试。需要几分钟，测试完后，将将显示问题列表。  
nose介绍文章 http://pythontesting.net/framework/nose/nose-introduction
  
### 开始pandas学习
进入python交互式模式，先导入模块
```python
>>> import pandas as pd  
>>> import numpy as np
```  
## pandas数据结构  
pandas核心为Series和DataFrame两大数据结构，两者特点将Index对象和标签整合到自己的结构中。  
- Series这类数据结构主要用于存储一个序列这要的以为数组。  
- DataFrame则用于存储多维数组。
###  一、Series对象  
Series对象用来表示一维数据结构，内部结构有两个相互关联的数组组成，主数组用来存放数据（NumPy任意数据类型）。主数组的每个元素都有一个与之对应的关联标签，这些标签存储在另一个叫做Index的数组中。  Series对象结构如下图：

index|value
-|-
0|12
1|-4
2|7

#### 1. 声明Series对象  
```python
# Series构造函数。声明Series时，若不指定index选项，默认使用从0开始依次递增的数值作为标签。此时标签与元素索引一致。常用方法传入一个list
def __init__(self, data=None, index=None, dtype=None, name=None,
                 copy=False, fastpath=False):

>>> s = pd.Series([12,-4,7],index=['a','b','c'])  #以数组形式传入，创建Series对象
>>> s
a    12
b    -4
c     7
dtype: int64
>>> s.values  #查看Series对象的两个数组，分别调用它的两个属性：values(元素)和index(索引)
array([12, -4,  7])
>>> s.index
Index(['a', 'b', 'c'], dtype='object')
```
#### 2.选择内部元素
把它作为普通NumPy数组，指定键即可。  
以上面的s为例: s[2]  、 s['b']、  s[0:2]  
```python
>>> s[['b','c']] # 特殊点：选取多个数据时，可以使用元素碎影标签
b   -4
c    7
dtype: int64
```
#### 3. 为元素赋值，不言自明 (>'_'<)
#### 4. 用Numpy数组或其他Series对象定义新的Series对象
```python
>>> arr = np.array([1,2,3,4]) # 创建Numpy数组
>>> s2 = pd.Series(arr) #以Numpy数组作为参数定义新的Series数组
>>> s2
0    1
1    2
2    3
3    4
dtype: int64
>>> s3 = pd.Series(s) #以Series数组定义新的数组
>>> s3
a    12
b    -4
c     7
dtype: int64
```
注意：这两种方式方法定义出的新数组并非是原数组元素的副本，而只是对他们的引用。换句话说，改变原有对象元素的值，新的Series对象中对应的元素值也会改变。

#### 5.筛选元素
pandas库的开发以NumPy库为基础，由此就数据结构而言，NumPy数组的多种操作方法扩展到Series对象中，筛选方法与NumPy一样。
```python
>>> s3[s3 > 0]
a    12
c     7
dtype: int64
```
#### 6.Series对象运算和数学函数
适用于NumPy数组的运算符（+ - * /）或其他数学函数也适用于Series对象，而NumPy库的数学函数学需要指明他们的出处。
```python
>>> s3/2
a    6.0
b   -2.0
c    3.5
dtype: float64
>>> np.log(s3)
a    2.484907
b         NaN
c    1.945910
dtype: float64
```
#### 7.Series对象运算和数学函数
对重复元素的操作，unique() 去重；value_counts() 去重并返回各元素出现次数；isin()判断所属关系。这些操作并不改变Series对象元素。
```python
>>> serd = pd.Series([1,2,1,2], index = ['a','a','b','c'])
>>> serd
a    1
a    2
b    1
c    2
dtype: int64
>>> serd.unique() #去除重复的元素，返回一个数组包含去重后的元素
array([1, 2])
>>> serd.value_counts()  #返回一个Series数组，Index为去重后结果，Vlaue为元素对应出现次数
2    2
1    2
dtype: int64
>>> serd.isin([0,1]) #判断一列元素时候包含在serd中
a     True
a    False
b     True
c    False
dtype: bool
>>> serd[serd.isin([0,1])] # 通过isin筛选出符合条件的元素
a    1
b    1
dtype: int64
```
#### 8.Series用作字典
```python
>>> mydict = {'a':10,'b':20,'c':30}
>>> myseries = pd.Series(mydict)
>>> myseries
a    10
b    20
c    30
dtype: int64
>>> myindex = ['b','c','a','d']
>>> myseries = pd.Series(mydict, index=myindex)
>>> myseries
b    20.0
c    30.0
a    10.0
d     NaN   # 没有的用NaN填充
dtype: float64
```
#### 9.Series对象之间的运算
Series能够识别标签对其不一致的数据，计算出的新Series对象中，只对标签相同的求和，不同的值为NaN
```python
>>> myseries2 = pd.Series(mydict)
>>> myseries + myseries2
a    20.0
b    40.0
c    60.0
d     NaN
dtype: float64
```
### 二、 DataFrame对象
DataFrame结构与Excel表格相似，结构示例如下：

index|color|object|price
-|-|-|-
0|blue|ball|1.2
1|green|pen|1.0
2|yellow|pencil|0.6

Series对象的Index数组存放每个元素的标签，而DataFrame对象有两个索引数组，第一个与行数据相关联，与Series索引数组相似，第二个数组与列数据相关联。

### 1. DataFrame的声明  
```python
# 创建DataFrame对象时，若不传入index，默认从0开始递增。常用方法传入一个dict，columns选项指定需要的列，值为一个list
def __init__(self, data=None, index=None, columns=None, dtype=None,
                 copy=False)

>>> data = {'color':['blue','yellow','green'],
...         'object':['ball','pen','paper'],
...         'price':[1.2,1.0,0.6]}
>>> frame = pd.DataFrame(data, columns=['object','price'])  #指定需要的列
>>> frame
  object  price
0   ball    1.2
1    pen    1.0
2  paper    0.6
>>> frame2 = pd.DataFrame(np.arange(9).reshape((3,3)),index=['a','b','c'],columns=['aa','bb','cc'])
>>> frame2
   aa  bb  cc
a   0   1   2
b   3   4   5
c   6   7   8
```

### 2. 选取元素  
```python
>>> frame2.columns   #获取所有列名称
Index(['aa', 'bb', 'cc'], dtype='object')
>>> frame2.index # 获取索引列表
Index(['a', 'b', 'c'], dtype='object')
>>> frame2.values # 获取存储在数据结构中的元素
array([[0, 1, 2],
       [3, 4, 5],
       [6, 7, 8]])
>>> frame2['aa'] #通过列名获取列，或者直接将列名称作为属性获取列
a    0
b    3
c    6
Name: aa, dtype: int64
>>> frame2.aa
a    0
b    3
c    6
Name: aa, dtype: int64
>>> frame2.ix[2] #通过ix属性和行的索引或标签获取行
aa    6
bb    7
cc    8
Name: c, dtype: int64
>>> frame2.ix[[1,2]] #选取多行
   aa  bb  cc
b   3   4   5
c   6   7   8
>>> frame2[0:1]  #指定索引范围选取多行
   aa  bb  cc
a   0   1   2
>>> frame2['aa'][2] #选取指定元素，通过列名和行索引或则标签
6
```
#### 3. 赋值
```python
# 通过name属性为index，columns指定标签
>>> frame2.index.name = 'id';frame2.columns.name='item'
>>> frame2
item  aa  bb  cc
id              
a      0   1   2
b      3   4   5
c      6   7   8
>>> frame2['new'] = 12 #添加一行，直接赋值；若要更新一列，需要把一个数组赋给这一列
>>> frame2
item  aa  bb  cc  new
id                   
a      0   1   2   12
b      3   4   5   12
c      6   7   8   12
```
#### 4. 元素间所属关系
用isin函数来判断。
```python
>>> frame2.isin([1,4,8]) #value在list中的为True
item     aa     bb     cc    new
id                              
a     False   True  False  False
b     False   True  False  False
c     False  False   True  False
>>> frame2[frame2.isin([1,4,8])]  # value不再list中的为NaN
item  aa   bb   cc  new
id                     
a    NaN  1.0  NaN  NaN
b    NaN  4.0  NaN  NaN
c    NaN  NaN  8.0  NaN
```

#### 5.删除一列
```python
>>> del frame2['new'] # 使用del 删除一列数据
```
#### 6. 筛选
```python
>>> frame2[frame2 >2] # 指定条件筛选，不符合条件的元素被替换成NaN
item   aa   bb   cc
id                 
a     NaN  NaN  NaN
b     3.0  4.0  5.0
c     6.0  7.0  8.0
>>> 
```
#### 7. 嵌套字典生成DataFrame对象 / DataFrame转置
```python
>>> data = {'red':{'i1':1,'i2':2},
... 'blue':{'i2':4}}
>>> frame = pd.DataFrame(data)  # 外部键解释成列，内部键解释为行，NaN填补确实元素
>>> frame
    blue  red
i1   NaN    1
i2   4.0    2
>>> frame.T   # pandas提供的一种简单的转置操作 (>'~'<)
       i1   i2
blue  NaN  4.0
red   1.0  2.0
```

### 三、Index对象
数据分析方面大多数优秀特性取决于整合到这些数据结构中的Index对象。
通过Series、DataFrame对象的index属性，可以将存储多个标签的数组转化为Index对象。
Index对象在声明后是不可变的，这保证了不同数据结构共用Index对象时，保证它的安全。
#### 1. Index对象中的几个方法
```python
>>> ser = pd.Series(np.arange(3),index=['a','c','c'])
>>> ser.idxmin()  #获取最小索引
'a'
>>> ser.idxmax()  #获取最大索引
'c'
>>> ser['c']  # 一个标签对应多个元素，通过标签选取时的到的是一个Series对象。此逻辑也适用于DataFrame
c    1
c    2
dtype: int64
>>> ser.index.is_unique # 调用该属性可以知道Series或者DataFrame中索引是否存在重复项
False
```
#### 2. 更换索引
数据结构一旦声明，Index对象就不能改变，但是执行更换索引操作可以解决这个问题。重新定义索引后，会新生成一个新的数据结构。
```python
>>> ser = pd.Series([2,5,7], index=['a','b','c'])
>>> ser
a    2
b    5
c    7
dtype: int64
>>> ser.reindex(['b','c','d']) # 更换索引，调整索引顺序，删除或者增添新标签。
b    5.0
c    7.0
d    NaN
dtype: float64

# 索引补全
>>> ser = pd.Series([1,5,7],index=[0,3,5])
# range(6)指定索引范围，method = ’ffill‘ 表示新插入的索引项对应的元素为前面索引编号比他小的那项元素,
# method = ’bfill‘ 则为后一项...
>>> ser.reindex(range(6), method='ffill') 
0    1
1    1
2    1
3    5
4    5
5    7
dtype: int64
# ** 更换索引的概念可以从Series扩展到DataFrame **
```
#### 3. 删除
pandas专门提供一个用于删除操作的函数： drop()， 它返回不包含已删除索引及其元素的新对象。  
```python
# 对Series的操作
def drop(self, labels, errors='raise') # Series 的drop源码接口
>>> ser = pd.Series(np.arange(4), index=['red','yellow','blue','white'])
>>> ser.drop('yellow') #删除一个标签
red      0
blue     2
white    3
dtype: int64
>>> ser.drop(['yellow','red']) # 删除多个
blue     2
white    3
dtype: int64

#对DataFrame的操作
def drop(self, labels=None, axis=0, index=None, columns=None, level=None,
             inplace=False, errors='raise') # DataFrame 中drop源码接口

>>> frame = pd.DataFrame(np.arange(9).reshape((3,3)),index=['yellow','red','white'],columns=['ball','pen','pencil'])
>>> frame
        ball  pen  pencil
yellow     0    1       2
red        3    4       5
white      6    7       8
>>> frame.drop('yellow') # 删除行，和Series一样
       ball  pen  pencil
red       3    4       5
white     6    7       8
>>> frame.drop(['ball','pen'],axis=1) #将axis指定为1，表示删除列
        pencil
yellow       2
red          5
white        8
```
#### 4. 算术和数据对齐
pandas能够将两个数据结构的索引对齐，索引相同的进行运算，不同的元素置为NaN。 
```python
>>> s1 = pd.Series(range(3), ['a','b','c'])
>>> s2 = pd.Series(range(2,5),['b','a','d'])
>>> s1 + s2
a    3.0
b    3.0
c    NaN
d    NaN
dtype: float64
>>> frame1 = pd.DataFrame(np.arange(9).reshape(3,3),index=['a','b','c'],columns=['huaji','hh','wxiao'])
>>> frame2 = pd.DataFrame(np.arange(2,11).reshape(3,3),index=['d','c','a'],columns=['blue','hh','huaji'])
>>> frame1 + frame2
   blue    hh  huaji  wxiao
a   NaN  10.0   10.0    NaN
b   NaN   NaN    NaN    NaN
c   NaN  13.0   13.0    NaN
d   NaN   NaN    NaN    NaN
```
### 四、数据结构之间的运算
####  1. 运算方法
- add()
- sub()
- div()
- mul()
分别对应加减乘除，上一个例子中的 frame1 + frame2 等价与 frame1.add(frame2)
#### 2. DataFrame  与 Series对象运算
Series与DataFrame运算，Series的索引对应DataFrame的列名，若相同则运算，不同则置为NaN
```python
>>> frame2 = pd.DataFrame(np.arange(2,11).reshape(3,3),index=['d','c','a'],columns=['blue','hh','huaji'])
>>> frame2
   blue  hh  huaji
d     2   3      4
c     5   6      7
a     8   9     10
>>> ser = pd.Series(range(4),['blue','huaji','hh','red']) 
>>> ser
blue     0
huaji    1
hh       2
red      3
dtype: int64
>>> ser + frame2   # ‘Series中的red’索引，DataFrame中不存在，则添加一列red
   blue  hh  huaji  red
d     2   5      5  NaN
c     5   8      8  NaN
a     8  11     11  NaN
```
### 五、函数应用和映射
#### 1.操作元素的函数
pandas以NumPy为基础，并对其进行功能扩展。通用函数（ufunc， universal funcation）由扩展得到，该类函数能对数据结构中的元素进行操作。
- np.sqrt()  
- ......
#### 2. 执行按行或列操作的函数
```python
def apply(self, func, axis=0, broadcast=False, raw=False, reduce=None,
              args=(), **kwds) # apply函数接口，axis默认为0，表示按行运算，func可以为自定义函数

frame = pd.DataFrame(np.arange(1,10).reshape(3,3),index=['a','b','c'],columns=['ball','pen','paper'])
def f(x):
     return x.max() - x.min()   # 返回值不一定需要是标量，也可以时Series对象

a = frame.apply(f, axis = 1) # 将axis置为1，函数处理的为列
b = frame.apply(f) # axis默认为0，函数处理行
print(a,'\n')
print(b)
-----------------输出：
   ball  pen  paper
a     1    2      3
b     4    5      6
c     7    8      9
a    2
b    2
c    2
dtype: int64 

ball     6
pen      6
paper    6
dtype: int64
```
#### 3. 统计函数
- sum()  元素和
- mean() 元素均值
- describe() 可以计算多个统计量
```python
frame = pd.DataFrame(np.arange(1,10).reshape(3,3),
                     index=['a','b','c'],columns=['ball','pen','paper'])
print(frame.describe())
--------------输出：
       ball  pen  paper
count   3.0  3.0    3.0
mean    4.0  5.0    6.0
std     3.0  3.0    3.0
min     1.0  2.0    3.0
25%     2.5  3.5    4.5
50%     4.0  5.0    6.0
75%     5.5  6.5    7.5
max     7.0  8.0    9.0
```
### 六、排序和排位次 、 相关性和协方差
pandas的sort_index()函数返回一个跟原对象元素相同但顺序不同的新对象，为按索引排序。
```python
#sort_index中，ascending默认为True升序，置为False则为降序，axis = 0 按行排序，为1按列。
#DataFrame中by可以指定那一列或者哪几列进行排序
def sort_index(self, axis=0, level=None, ascending=True, inplace=False,
                   kind='quicksort', na_position='last', sort_remaining=True,
                   by=None)  # DataFrame 
def sort_index(self, axis=0, level=None, ascending=True, inplace=False,
                   kind='quicksort', na_position='last', sort_remaining=True) # Series 
```
```python
>>> ser = pd.Series((2,1,3),index=['a','b','c'])
>>> ser.sort_index() # 按索引排序
a    2
b    1
c    3
dtype: int64
>>> ser.sort_values() # 对元素排序
b    1
a    2
c    3
dtype: int64

>>> data = {'color':['blue','yellow','green'],
...         'object':['ball','pen','paper'],
...         'price':[1.2,1.0,0.6]}
>>> frame = pd.DataFrame(data)
>>> frame
    color object  price
0    blue   ball    1.2
1  yellow    pen    1.0
2   green  paper    0.6
>>> frame.sort_index(ascending = False) #按索引逆序排序
    color object  price
2   green  paper    0.6
1  yellow    pen    1.0
0    blue   ball    1.2
>>> frame.sort_index(by=['color','price']) # 选取color、price两列按元素排序
    color object  price
0    blue   ball    1.2
2   green  paper    0.6
1  yellow    pen    1.0

# 排位次操作
def rank(self, axis=0, method='average', numeric_only=None,
             na_option='keep', ascending=True, pct=False) #在generic.py下，Series、DataFrame共用实现。
#rank 排位次操作，为每个元素排一个位次，初始值为1，默认升序按行排，位次越小，元素值越小
>>> ser.rank()
a    2.0
b    1.0
c    3.0
dtype: float64
>>> frame.rank()
   color  object  price
0    1.0     1.0    3.0
1    3.0     3.0    2.0
2    2.0     2.0    1.0
```

#### 5. 相关性和协方差
- corr()  相关性，correlation
- cov()   协方差，covariance
这两个操作通常涉及两个Series对象
```python
>>> ser = pd.Series(range(1,5),range(6,10))
>>> ser1 = pd.Series(range(2,6),range(7,11))
>>> ser.corr(ser1)
1.0
>>> ser.cov(ser1)
1.0

>>> frame = pd.DataFrame(np.arange(9).reshape(3,3),columns=['a','b','c'])
>>> frame.corr()
     a    b    c
a  1.0  1.0  1.0
b  1.0  1.0  1.0
c  1.0  1.0  1.0
>>> frame.cov()
     a    b    c
a  9.0  9.0  9.0
b  9.0  9.0  9.0
c  9.0  9.0  9.0

# corrwith() 计算DataFrame对象的列或行与series对象或其他DataFrame对象元素两两之间的关系
>>> frame.corrwith(ser)    # ......
a   NaN
b   NaN
c   NaN
dtype: float64
```

### 七、 NaN数据
有些元素在数组中没有定义，用NaN表示。  
pandas在计算各种统计量是，没有考虑NaN值，因此需要对缺失值的进行处理。
#### 1. 为元素赋NaN值，使用 np.NaN 或者 np.nan 即可
#### 2. 过滤NaN
数据分析过程中，有几种去除NaN的方法。
```python
# 对于Ｓeries
>>> ser = pd.Series([1,2,np.nan,np.NaN], index=['a','b','c','d'])
>>> ser
a    1.0
b    2.0
c    NaN
d    NaN
dtype: float64
>>> ser.dropna()
a    1.0
b    2.0
dtype: float64
>>> ser[ser.notnull()]
a    1.0
b    2.0
dtype: float64

# 对于ＤataＦrame，若采用dropna(),只要行列中有一个为ＮaN，则该行或该列元素都会被删除
#因此需要使用how选项，指定其值为‘all’，当所有元素为ＮaN的行或列，才会被删除
frame.dropna(how = 'all')
```
#### 3. 为ＮaN填充其他值
删除ＮaN元素有着跟着删除其他数据分析相关数据的风险，因此可以使用fillall函数用其他数据替代ＮaN。
```python
>>> frame = pd.DataFrame([[np.NaN,np.NaN,np.NaN],
...                       [np.NaN,np.NaN,np.NaN],
...                       [np.NaN,np.NaN,np.NaN]],columns=['ball','mug','open'])
>>> 
>>> frame.fillna(0)
   ball  mug  open
0   0.0  0.0   0.0
1   0.0  0.0   0.0
2   0.0  0.0   0.0
>>> frame.fillna({'ball':1,'mug':0,'open':99})
   ball  mug  open
0   1.0  0.0  99.0
1   1.0  0.0  99.0
2   1.0  0.0  99.0
```
### 八、等级索引和分级
等级索引是pandas的一个重要功能，单条轴可以有多级索引。
```python
# 包含两条索引的Ｓeries对象，创建一个包含两层数据的结构
>>> mser = pd.Series(np.random.rand(6),index=[['1','1','2','3','3','3'],
...                                           ['11','12','13','21','22','23']])
>>> mser
1  11    0.520811
   12    0.863611
2  13    0.341452
3  21    0.532431
   22    0.704528
   23    0.855665
dtype: float64

>>> mser['1'] # 通过索引取值
11    0.520811
12    0.863611
dtype: float64
>>> mser[:,'12']
1    0.863611
dtype: float64
>>> mser['1','11'] # 取特定元素
0.52081116144978901

# 等级索引在调整基于组的操作（比如创建数据透视表）方面起重要作用。
# 使用unstack()函数调整ＤataFrame中的数据，把等级索引Ｓeries转换成一个ＤataFrame对象。
>>> mser.unstack()
         11        12        13        21        22        23
1  0.520811  0.863611       NaN       NaN       NaN       NaN
2       NaN       NaN  0.341452       NaN       NaN       NaN
3       NaN       NaN       NaN  0.532431  0.704528  0.855665
```
 相应的把ＤataFrame对象转换成Ｓeries对象，使用函数stack函数  
 对于ＤataＦrame对象，可以指定行列都定义为等级索引，操作与上面类似。
```python
>>>mframe = pd.DataFrame(np.random.rand(16).reshape(4,4),
                      index=[['white','white','red','red'],['up','down','up','down']],
                      columns=[['pen','pen','paper','paper'],[1,2,1,2]])
>>> mframe
                 pen               paper          
                   1         2         1         2
white up    0.803801  0.877716  0.485271  0.296192
      down  0.951472  0.683435  0.160152  0.788390
red   up    0.912280  0.342962  0.210265  0.641238
      down  0.406704  0.869501  0.172877  0.871064
>>> mframe.stack()
                 paper       pen
white up   1  0.883151  0.608774
           2  0.922328  0.582680
      down 1  0.997468  0.950563
           2  0.671081  0.319449
red   up   1  0.512799  0.111164
           2  0.919113  0.718317
      down 1  0.939946  0.185754
           2  0.625839  0.153314
```
#### 1. 重新调整顺序和为层级排序
swaplevel() 函数以要互换位置的两个层级的名称为参数，返回交换位置后的一个新对象，其中各元素的顺序保持不变。
```python
>>> mframe.columns.names = ['object','id']
>>> mframe.index.names=['color','status']
>>> mframe
object             pen               paper          
id                   1         2         1         2
color status                                        
white up      0.803801  0.877716  0.485271  0.296192
      down    0.951472  0.683435  0.160152  0.788390
red   up      0.912280  0.342962  0.210265  0.641238
      down    0.406704  0.869501  0.172877  0.871064
>>> mframe.swaplevel('color','status')  # 默认交换行，axis设置为1，交换列
object             pen               paper          
id                   1         2         1         2
status color                                        
up     white  0.803801  0.877716  0.485271  0.296192
down   white  0.951472  0.683435  0.160152  0.788390
up     red    0.912280  0.342962  0.210265  0.641238
down   red    0.406704  0.869501  0.172877  0.871064
>>> mframe.sortlevel('color') # 只根据一个层级对数据排序
__main__:1: FutureWarning: sortlevel is deprecated, use sort_index(level= ...)
object             pen               paper          
id                   1         2         1         2
color status                                        
red   down    0.406704  0.869501  0.172877  0.871064
      up      0.912280  0.342962  0.210265  0.641238
white down    0.951472  0.683435  0.160152  0.788390
      up      0.803801  0.877716  0.485271  0.296192
```
#### 2. 按层统计数据
ＤataFrame或Ｓeries对象中很多描述性和概括统计量都有level选项，可以通过指定要获取那个层级的描述性和概括统计量。
```python
# 举个栗子
>>> mframe.sum(level = 'color')
object       pen               paper          
id             1         2         1         2
color                                         
white   1.755273  1.561152  0.645423  1.084582
red     1.318984  1.212463  0.383142  1.512302
```





