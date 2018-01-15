## 链表常见操作总结
1. 获得链表中间点
快慢指针法：
```java
public ListNode getMiddle(ListNode head){
  ListNode slow = head;
  ListNode fast = head;
  // ListNode fast = head.next;

  while(fast.next != null && fast.next.next != null){
    slow = slow.next;
    fast = fast.next.next;
  }

  return slow;
}
```
注: 快慢指针法中,fast值取head 与 取head.next的区别:
- 当链表元素为偶数个时,两者情况下的slow所指元素相同，位置为第n/2。例如: 链表 1 -> 2 -> 3 -> 4, 两者情况下slow都指向 2
- 当链表元素为奇数时,fast值取head下slow所指向的元素(n/2 + 1)为fast取head.next下的slow.next。例如: 指向的例如 1 -> 2 -> 3,前者slow指向2，后者slow指向1。

2. 链表排序 [归并和快排](https://github.com/lunarku/learn/blob/master/leetcode/src/list/code/Sort_List.java)
