# retwis
A website to simulate weibo which use redis as data storge


## key-value设计
the key-value database desigin:

**user-table:**
- user:userid:1:username  xiaoxiong
- user:userid:1:password  111
- user:username:xiaoxiong:userid  1
  - to find the user-info with "userid",we could not bypass
    redundant key
- lpush users:userids userid
  - to maintain the newest register users
- ltrim users:userids 0 50

**post-table:** store through string-structure(used)
- post:postid:1:time    timestamp
- post:postid:1:content content
- post:postid:1:userid  1

given "userid" which we can locate the user-info
and  to record weibo-status,the List\<status\> seems
be helpful,with the list-data-structure,
we can store the userid and postids,such as:
- lpush statuses:userid:1:postids 1 2
- lrange statuses:userid:1:postids 0 -1

**post-table:** store through hash-structure(non-used)
- post:postid:1 time timestamp
- post:postid:1 content content
- post:postid:1 userid 1

get the postid through userid which we have stored in statuses such as:
- lpush statuses:userid:1:postids 1 2

**fans-table**
- sadd fans:userid:1:fansids 2
- lpush concers:userids 1 


## 展示页面

![登录页面](/git_pictures/login.png)

![微博发表](/git_pictures/home.png)

![热点话题](/git_pictures/timeline.png)

![个人主页](/git_pictures/profile1.png)

![个人主页](/git_pictures/profile.png)





