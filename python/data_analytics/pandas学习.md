[TOC]
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



