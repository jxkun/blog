 # numpy使用
numpy是用于python进行科学计算，数据分析时所用到的一个基础库，是大量python数学和科学计算包的基础。
## 环境搭建
### 1. numpy的安装
- linux下  
>ubuntu 
sudo apt-get install python-numpy
fedora
sudo yum install python-numpy

-Anaconda 
> coda install numpy

### 2. 导入
```python
>>> import numpy as np
``` 
## 基本概念  
1. numpy库的基础是ndarray（N-dimensional array，N维数组）对象。ndarray对象是一种由同质（同质是指几乎所有元素的类型和大小都相同）元素组成的多维数组，元素数量事先指定好。数据类型由一个叫做dtype的numpy对象指定；每个ndarray只有一种dtype。
2. 数组的维数和元素的数量由数组的型（shape）确定，shape由N个正整数组成的元组来指定，元组中的每一个元素对应每一维的大小。数组的维统称为轴（axes），轴的数量被称作为秩（rank）。
3. numpy数组特点，大小固定。

### 三个概念
- 对象的视图与副本
赋值运算切片都不会创建副本，返回的对象只是元素组的视图。  
创建副本需要使用copy()函数，例如： c = a.copy()
- 向量化
- 广播机制
广播机制实现了对两个或两个以上的数组进行运算或者函数处理，即使两个数组形状并不完全相同。  
广播条件： 若两个数组的各维度兼容，也就是两个数组的每一维等长，或者其中一个数组为一维，那么广播机制就适用；若两者都不满足，则会抛出异常，表示两个数组不兼容。
>广播机制两条规则：
>1. 为缺失的维度补上个1。例如：
shape为(4, 4)的数组与shape为(4)的数组运算，将shape为(4)的补为(4, 1)
>2. 假定缺失的元素都用已有值进行填充。例如：
a = np.arange(16).reshape(4,4)
b = np.arange(4)
a的值为:
array([[ 0,  1,  2,  3],
       [ 4,  5,  6,  7],
       [ 8,  9, 10, 11],
       [12, 13, 14, 15]])
b的值为:
array([0, 1, 2, 3])
进行a + b运算时，查看广播规则1，缺失维度补1,  4 --> 4 x 1  
查看广播规则2： 缺失元素用已有元素填充，填充后为：
array([[0, 1, 2, 3]
       [0, 1, 2, 3]
       [0, 1, 2, 3]
       [0, 1, 2, 3]])
然后进行运算。

```python
# 广播例子
>>> a = np.arange(9).reshape(3,3)
>>> b = np.arange(3)
>>> a + b
array([[ 0,  2,  4],
       [ 3,  5,  7],
       [ 6,  8, 10]])

>>> m = np.arange(6).reshape(3,1,2)
>>> n = np.arange(6).reshape(3,2,1)
>>> m
array([[[0, 1]],

       [[2, 3]],

       [[4, 5]]])
>>> n
array([[[0],
        [1]],

       [[2],
        [3]],

       [[4],
        [5]]])
>>> n + m
array([[[ 0,  1],
        [ 1,  2]],

       [[ 4,  5],
        [ 5,  6]],

       [[ 8,  9],
        [ 9, 10]]])
```

### 1. 创建数组
可以通过array函数创建和自带函数创建
- 通过array函数创建数组
```python
# array函数
def array(p_object, dtype=None, copy=True, order='K', subok=False, ndmin=0): # real signature unknown; restored from __doc__
    """
    array(object, dtype=None, copy=True, order='K', subok=False, ndmin=0)
    
        Create an array.
    
        Parameters
        ----------
        object : array_like
            An array, any object exposing the array interface, an object whose
            __array__ method returns an array, or any (nested) sequence.
        dtype : data-type, optional
            The desired data-type for the array.  If not given, then the type will
            be determined as the minimum type required to hold the objects in the
            sequence.  This argument can only be used to 'upcast' the array.  For
            downcasting, use the .astype(t) method.
        copy : bool, optional
            If true (default), then the object is copied.  Otherwise, a copy will
            only be made if __array__ returns a copy, if obj is a nested sequence,
            or if a copy is needed to satisfy any of the other requirements
            (`dtype`, `order`, etc.).
        order : {'K', 'A', 'C', 'F'}, optional
            Specify the memory layout of the array. If object is not an array, the
            newly created array will be in C order (row major) unless 'F' is
            specified, in which case it will be in Fortran order (column major).
            If object is an array the following holds.
    
            ===== ========= ===================================================
            order  no copy                     copy=True
            ===== ========= ===================================================
            'K'   unchanged F & C order preserved, otherwise most similar order
            'A'   unchanged F order if input is F and not C, otherwise C order
            'C'   C order   C order
            'F'   F order   F order
            ===== ========= ===================================================
    
            When ``copy=False`` and a copy is made for other reasons, the result is
            the same as if ``copy=True``, with some exceptions for `A`, see the
            Notes section. The default order is 'K'.
        subok : bool, optional
            If True, then sub-classes will be passed-through, otherwise
            the returned array will be forced to be a base-class array (default).
        ndmin : int, optional
            Specifies the minimum number of dimensions that the resulting
            array should have.  Ones will be pre-pended to the shape as
            needed to meet this requirement.
    
        Returns
        -------
        out : ndarray
            An array object satisfying the specified requirements.
    
        See Also
        --------
        empty, empty_like, zeros, zeros_like, ones, ones_like, full, full_like
    
        Notes
        -----
        When order is 'A' and `object` is an array in neither 'C' nor 'F' order,
        and a copy is forced by a change in dtype, then the order of the result is
        not necessarily 'C' as expected. This is likely a bug.
    
        Examples
        --------
        >>> np.array([1, 2, 3])
        array([1, 2, 3])
    
        Upcasting:
    
        >>> np.array([1, 2, 3.0])
        array([ 1.,  2.,  3.])
    
        More than one dimension:
    
        >>> np.array([[1, 2], [3, 4]])
        array([[1, 2],
               [3, 4]])
    
        Minimum dimensions 2:
    
        >>> np.array([1, 2, 3], ndmin=2)
        array([[1, 2, 3]])
    
        Type provided:
    
        >>> np.array([1, 2, 3], dtype=complex)
        array([ 1.+0.j,  2.+0.j,  3.+0.j])
    
        Data-type consisting of more than one element:
    
        >>> x = np.array([(1,2),(3,4)],dtype=[('a','<i4'),('b','<i4')])
        >>> x['a']
        array([1, 3])
    
        Creating an array from sub-classes:
    
        >>> np.array(np.mat('1 2; 3 4'))
        array([[1, 2],
               [3, 4]])
    
        >>> np.array(np.mat('1 2; 3 4'), subok=True)
        matrix([[1, 2],
                [3, 4]])
    """
    pass
```
- 通过自带的数组创建数组
zeros()  创建元素均为零的数组（默认float类型）
ones()  创建元素均为1的数组（默认float类型）
arange()  类似range()
random() 使用随机数填充数组
```python
def zeros(shape, dtype=None, order='C')
def ones(shape, dtype=None, order='C')
def arange(start=None, stop=None, step=None, dtype=None):

>>> np.zeros((3,3))
array([[ 0.,  0.,  0.],
       [ 0.,  0.,  0.],
       [ 0.,  0.,  0.]])
>>> np.arange(0,9)
array([0, 1, 2, 3, 4, 5, 6, 7, 8])
>>> np.arange(0,9).reshape(3,3) # reshape 改变数组维度(重新调整矩阵的行数、列数、维数。)
array([[0, 1, 2],
       [3, 4, 5],
       [6, 7, 8]])
>>> np.random.random((3,3)) # np.random.random(9) 生成一维的数组
array([[ 0.2581092 ,  0.59997171,  0.39382817],
       [ 0.15688302,  0.11845211,  0.10310699],
       [ 0.84677699,  0.32978471,  0.85367526]])
```
### 2. 常用属性
```python
>>> a = np.random.random((2,2))
>>> type(a)  # 判断是否为ndarray
<class 'numpy.ndarray'>
>>> a.ndim # 轴的数量，即秩
2
>>> a.size # 数组长度
4
>>> a.shape # 数组的型，可以用来设置数组的维度
(2, 2)
>>> a.dtype # 数组数据类型
dtype('float64')
>>> a.itemsize #数组中每个元素的字节长度
8
>>> a.data   #包含数组实际元素的缓冲区
<memory at 0x000001AB99F70CF0>
>>> a.nbytes  # 整个数组所占用的存储空间（itemsize与size的乘积）
32
>>> a.T  # 转置，效果和transpose函数一样
array([[ 0.54470183,  0.47496048],
       [ 0.17784732,  0.40630741]])
```
### 3. numpy支持的数据类型
创建数组时，可以用dtype选项指定数据类型。
>bool_ Boolean (True or False) stored as a byte 
int_ Default integer type (same as C long; normally either int64 or int32) 
intc Identical to C int (normally int32 or int64) 
intp Integer used for indexing (same as C ssize_t; normally either int32 or int64) 
int8 Byte (-128 to 127) 
int16 Integer (-32768 to 32767) 
int32 Integer (-2147483648 to 2147483647) 
int64 Integer (-9223372036854775808 to 9223372036854775807) 
uint8 Unsigned integer (0 to 255) 
uint16 Unsigned integer (0 to 65535) 
uint32 Unsigned integer (0 to 4294967295) 
uint64 Unsigned integer (0 to 18446744073709551615) 
float_ Shorthand for float64. 
float16 Half precision float: sign bit, 5 bits exponent, 10 bits mantissa 
float32 Single precision float: sign bit, 8 bits exponent, 23 bits mantissa 
float64 Double precision float: sign bit, 11 bits exponent, 52 bits mantissa 
complex_ Shorthand for complex128. 
complex64 Complex number, represented by two 32-bit floats (real and imaginary components) 
complex128 Complex number, represented by two 64-bit floats (real and imaginary components)

## numpy数组操作
### 1. 元素级运算（指作用于位置相同的元素之间）
- 数组 与 标量之间的加减乘除（+-*/）
- 数组与数组之间的加减乘除
```python
>>> a = np.arange(9).reshape(3,3)
>>> b = np.arange(9).reshape(3,3)
>>> a+b
array([[ 0,  2,  4],
       [ 6,  8, 10],
       [12, 14, 16]])
```
### 2. 矩阵积
注意： *在numpy库中为元素级操作，不表示矩阵积，矩阵积用dot()函数表示。矩阵积不支持交换律。
```python
# 用上面的 a、b
>>> a.dot(b)
array([[ 15,  18,  21],
       [ 42,  54,  66],
       [ 69,  90, 111]])
```
### 3. 自增与自减
python中没有--和++，numpy库中同样没有。使用的为 += 和 -=运算符。
```python
>>> a = np.arange(9)
>>> a += 2
>>> a
array([ 2,  3,  4,  5,  6,  7,  8,  9, 10])
```
### 4. 通用函数
通用函数被称之为（universal function），通常叫做func。它对数组中的各个元素逐一进行操作。
```python
>>> a = np.arange(9).reshape(3,3)
>>> a
array([[0, 1, 2],
       [3, 4, 5],
       [6, 7, 8]])
>>> np.sqrt(a)
array([[ 0.        ,  1.        ,  1.41421356],
       [ 1.73205081,  2.        ,  2.23606798],
       [ 2.44948974,  2.64575131,  2.82842712]])
>>> np.log(a)
__main__:1: RuntimeWarning: divide by zero encountered in log
array([[       -inf,  0.        ,  0.69314718],
       [ 1.09861229,  1.38629436,  1.60943791],
       [ 1.79175947,  1.94591015,  2.07944154]])
>>> np.sin(a)
array([[ 0.        ,  0.84147098,  0.90929743],
       [ 0.14112001, -0.7568025 , -0.95892427],
       [-0.2794155 ,  0.6569866 ,  0.98935825]])
```
### 5. 聚合函数
聚合函数指对一组值进行操作，返回一个单一值作为结果的函数。ndarray类实现了很多这样的函数。
```python
>>> a = np.array([3.3,4.5,1.2,5.7,0.3])
>>> a.sum()
15.0
>>> a.min()
0.29999999999999999
>>> a.max()
5.7000000000000002
>>> a.mean() # 平均值
3.0
>>> a.std() # 标准差
2.0079840636817816
```

### 6索引切片机制、迭代、条件和布尔数组
1. 索引机制与python中的数组的相同。与python中的相比，新增的功能为，可以传入多个索引而同时选中多个元素。
```python
>>> a = np.arange(9).reshape(3,3)
>>> a
array([[0, 1, 2],
       [3, 4, 5],
       [6, 7, 8]])
>>> a[[1,2]]
array([[3, 4, 5],
       [6, 7, 8]])
```
2. 切片操作  
对于python列表进行切片操作得到的数组时元素组的副本，而对numpy切片操作的到的是指向相同缓冲区的视图。切片语法与python相同。
```python
>>> a = np.arange(9).reshape(3,3)
>>> a[0:2,0:2]
array([[0, 1],
       [3, 4]])
```
3. 迭代
python中迭代使用for...in...，而numpy也可以使用for...in...  
- numpy数组遍历矩阵每一个元素，可以用for循环遍历a.flat
- 除了for循环，可以使用numpy提供的 apply_along_axis() 函数
```python
# for循环遍历a.flat
>>> for i in a.flat:
...     print(i) # 输出0到8

# 使用np.apply_along_axis函数
def apply_along_axis(func1d, axis, arr, *args, **kwargs):
    """ Parameters
    ----------
    func1d : function # 该函数可以自定义
        This function should accept 1-D arrays. It is applied to 1-D
        slices of `arr` along the specified axis.
    axis : integer  # axis选项值为0，按列进行迭代操作，处理元素；为1，则按行
    arr : ndarray  # 输入的数组
    args : any
        Additional arguments to `func1d`.
    kwargs : any
        Additional named arguments to `func1d`.  """

# example
def foo(x):
    return x/2
print(np.apply_along_axis(foo, axis= 0, arr=a))
----------- 输出
[[ 0.   0.5  1. ]
 [ 1.5  2.   2.5]
 [ 3.   3.5  4. ]]
```
4. 条件与布尔数组
```python
>>> a = np.random.random((3,3))
>>> a
array([[ 0.90306312,  0.66149029,  0.9155267 ],
       [ 0.82132487,  0.85615931,  0.36354917],
       [ 0.28869754,  0.23805817,  0.94596346]])
>>> a < 0.5
array([[False, False, False],
       [False, False,  True],
       [ True,  True, False]], dtype=bool)
>>> a[a<0.5]
array([ 0.36354917,  0.28869754,  0.23805817])
```
## 数组形状变换及切分
### 形状变换
1. 数组行传变换
- reshape 改变数组维度(重新调整矩阵的行数、列数、维数。)  
- resize,resize和reshape函数的功能一样，但resize会直接修改所操作的数组
- ravel 函数完成展平，返回数组视图
- flatten 与ravel函数的功能相同。但是flatten函数会请求分配内存来保存结果
-  直接改变shape属性
```python
>>> arr = np.random.random(9)
>>> arr.reshape((3,3)) # 仅返回视图
array([[ 0.46050131,  0.42752715,  0.7875903 ],
       [ 0.13249693,  0.57989729,  0.36913722],
       [ 0.92541491,  0.42439856,  0.72596571]])
>>> arr
array([ 0.46050131,  0.42752715,  0.7875903 ,  0.13249693,  0.57989729,
        0.36913722,  0.92541491,  0.42439856,  0.72596571])
>>> arr.resize((3,3))  #直接修改数组
>>> arr
array([[ 0.46050131,  0.42752715,  0.7875903 ],
       [ 0.13249693,  0.57989729,  0.36913722],
       [ 0.92541491,  0.42439856,  0.72596571]])

>>> arr.ravel() 
array([ 0.46050131,  0.42752715,  0.7875903 ,  0.13249693,  0.57989729,
        0.36913722,  0.92541491,  0.42439856,  0.72596571])
>>> arr.flatten() 
array([ 0.46050131,  0.42752715,  0.7875903 ,  0.13249693,  0.57989729,
        0.36913722,  0.92541491,  0.42439856,  0.72596571])
```
2. 转置a.T 或者 a.transpose()函数

### 切分与连接
1. 连接数组
把多个数组整合在一起形成一个包含这些数组的新数组。numpy使用栈这个概念。
- vstack() 垂直入栈，把第二个数组的行添加到第一个数组
- hstack() 水平入栈，把第二个数组作为列添加到第一个数组
- column_stack()  将多个一维数组作为列压入栈结构 
- row_stack()  将多个一维数组作为行压入栈结构 
- 深度组合dstack函数(将一系列数组沿着纵轴（深度）方向进行层叠组合)
```python
>>> a = np.arange(3)
>>> b = np.arange(3,6)
>>> np.vstack((a,b))
array([[0, 1, 2],
       [3, 4, 5]])
>>> np.hstack((a,b))
array([0, 1, 2, 3, 4, 5])
>>> np.column_stack((a,b))
array([[0, 3],
       [1, 4],
       [2, 5]])
```
2. 数组切分
- 水平分割 ： 指把数组按宽度分割
hsplit函数
调用split函数并指定参数axis=1
- 垂直分割, ： 指把数组按高度分割
vsplit函数
调用split函数并指定参数axis=0
- 深度分割,dsplit函数
```python
>>> A = np.arange(9).reshape(3,3)
>>> np.hsplit(A,3)
[array([[0],
       [3],
       [6]]), array([[1],
       [4],
       [7]]), array([[2],
       [5],
       [8]])]
>>> np.vsplit(A,3)
[array([[0, 1, 2]]), array([[3, 4, 5]]), array([[6, 7, 8]])]
>>> np.split(A,3,axis=0)
[array([[0, 1, 2]]), array([[3, 4, 5]]), array([[6, 7, 8]])]

>>> b = np.arange(8).reshape(2,2,2)
>>> b
array([[[0, 1],
        [2, 3]],

       [[4, 5],
        [6, 7]]])
>>> np.dsplit(b,2) # 深度分割
[array([[[0],
        [2]],

       [[4],
        [6]]]), array([[[1],
        [3]],

       [[5],
        [7]]])]
```

### 结构化数组
结构化数组的列元素被指定为指定的数据类型。结构化数组中的元素被称之为结构体。使用dtype选项知名组成结构体的元素及其他们的数据类型和顺序。

数据类型|说明符
----------|-------------
bytes      | b1
int          | i1, i2, i4, i8
unsigned ints | u1, u2, u4, u8
floats     | f2, f4, f8
complex | c8, c16
fixed length strings| a[n]

```python
>>> struct = np.array([(1,'first',1 + 2j),(2,'second',2 + 3j),(3,'third',4 + 5j)],dtype=('i2,a6,c8'))
>>> struct
array([(1, b'first',  1.+2.j), (2, b'second',  2.+3.j),
       (3, b'third',  4.+5.j)],
      dtype=[('f0', '<i2'), ('f1', 'S6'), ('f2', '<c8')])  # 默认为列分配名称从 f0 到 fn
>>> struct['f1'] # 通过列名称取对应列
array([b'first', b'second', b'third'],
      dtype='|S6')
# 自定义列名称
>>> struct = np.array([(1,'first',1 + 2j),(2,'second',2 + 3j),(3,'third',4 + 5j)],
                                    dtype=[('num','i2'),('order','a6'),('complex','c8')])
>>> struct
array([(1, b'first',  1.+2.j), (2, b'second',  2.+3.j),
       (3, b'third',  4.+5.j)],
      dtype=[('num', '<i2'), ('order', 'S6'), ('complex', '<c8')])
>>> struct.dtype.names = ('id','name','c')  # 重新定义结构化数组的列名
>>> struct
array([(1, b'first',  1.+2.j), (2, b'second',  2.+3.j),
       (3, b'third',  4.+5.j)],
      dtype=[('id', '<i2'), ('name', 'S6'), ('c', '<c8')])
```

## 数据文件读写
1. 二进制文件的读写
numpy中save()方法以二进制格式保存数据，load()方法则从二进制文件中读取数据。
```python
data = np.random.random(16).reshape(4,4)
np.save('save_data',data) # 保存数组，系统会为文件自动添加.npy扩展名
load_data = np.load('save_data.npy') #加载数组需要手动加上扩展名
print(load_data)
-----------输出:
[[ 0.11249333  0.99842529  0.31492913  0.97365802]
 [ 0.91019606  0.21764636  0.67141618  0.64391277]
 [ 0.23092241  0.38862242  0.36544235  0.12910827]
 [ 0.46348153  0.22054371  0.85364239  0.87678187]]
```
2. 读取文件中的列表形式数据
读写文本格式数据（如TXT或CSV）。numpy的genfromtxt()函数可以从文件中读取数据，并将其插入数组中。通常这个函数接受三个参数，分别为 存放数据的文件名，用于分割值的字符和是否含有标签。例如：读取一个data.csv文件
```python
#data.csv中数据如下
id,value1,value2,value3
1,123,1.4,23
2,110,,18
3,,2.1,13

>>> data = np.genfromtxt('data.csv',delimiter=',', names = True) 
>>> data # 读取文件数据，空的项用nan填充
array([( 1.,  123.,  1.4,  23.), ( 2.,  110.,  nan,  18.),
       ( 3.,   nan,  2.1,  13.)],
      dtype=[('id', '<f8'), ('value1', '<f8'), ('value2', '<f8'), ('value3', '<f8')])
```






  