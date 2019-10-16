### 该项目用于演示如何将springboot2整合elasticsearch7.X

> 客户端采用官方推荐的highLevelClient

##### 原因
- transportClient,速度太慢，连官方都嫌弃它了。在 7.x 中已经被弃用，8.x 中将完全删除
- jest 毕竟不是官方的，更新速度较慢
