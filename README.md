# 东软智慧社区系统 (SmartCommunitySystem)

基于 Spring Cloud Alibaba 微服务架构的智慧社区平台，涵盖社区管理、商城交易、物业缴费、报修投诉等综合功能。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 运行环境 |
| Spring Boot | 3.2.5 | 核心框架 |
| Spring Cloud | 2023.0.1 | 微服务生态 |
| Spring Cloud Alibaba | 2023.0.1.0 | Nacos 服务发现 |
| Spring Cloud Gateway | - | API 网关 |
| MyBatis-Plus | 3.5.5 | ORM 持久化 |
| MySQL | 8.0 | 关系型数据库 |
| Redis | 7.x | 缓存 |
| Elasticsearch | 8.x | 商品搜索 |
| RabbitMQ | 3.x | 订单超时消息 |
| JJWT | 0.11.5 | JWT 鉴权 |
| BCrypt | - | 密码加密 |

**前端：** Vue 3 + Vite + Element Plus + Pinia + Axios

## 项目结构

```
smart-community-system/
├── sc-common/          # 公共模块（Result/VO/JWT/全局异常处理）
├── sc-gateway/         # API 网关 (8080) — 路由转发 + JWT 全局鉴权
├── sc-auth/            # 认证服务 (8100) — 登录/注册/密码找回
├── sc-user/            # 用户服务 (8200) — 个人信息/钱包/后台管理
├── sc-mall/            # 商城服务 (8300) — 分类/商品/店铺/库存
├── sc-trade/           # 交易服务 (8400) — 购物车/订单/支付
├── sc-community/       # 社区服务 (8500) — 公告/访客/报修/投诉/物业费
├── sc-search/          # 搜索服务 (8600) — 商品全文检索(ES)
├── sc-admin/           # 管理后台前端 (Vue 3)
└── sc-app/             # 居民端前端 (Vue 3)
```

## 架构图

```
                 sc-gateway (:8080)
                 JWT Auth Filter
                 Route StripPrefix=1
                      │
    ┌─────┬──────┬─────┼──────┬──────┬─────┐
    │     │      │     │      │      │     │
  auth  user  mall  trade  comm  search  common
  :8100 :8200 :8300 :8400  :8500  :8600  (lib)
    │     │      │     │      │      │
   MySQL MySQL MySQL MySQL  MySQL    ES
                        │      │
                     RabbitMQ   │
                                │
  sc-admin (:5173) ─────────────┘
  sc-app   (:5174) ─────────────┘
```

## 功能模块

### 认证服务 (sc-auth)
- 手机号+密码注册登录，BCrypt 密码加密
- JWT Token 签发（HS256，2小时有效期）
- 登录日志记录
- 自动创建用户钱包

### 用户服务 (sc-user)
- 个人信息查询/修改、密码修改
- 钱包余额管理、流水记录
- 后台用户管理（列表/启用禁用/角色分配）
- 内部钱包扣款/退款接口

### 商城服务 (sc-mall)
- 商品分类树管理
- 商品 CRUD（同步 Elasticsearch 索引）
- 店铺管理、店铺商品关联
- 内部库存锁定/解锁/确认接口

### 交易服务 (sc-trade)
- 购物车（增删改查/勾选）
- 订单创建（锁库存 → 生成订单 → 延迟消息）
- 钱包支付 / 取消订单（解锁库存 / 退款）
- 订单状态流：待支付 → 待发货 → 待取货 → 已完成
- 支持到店自提和配送两种方式
- 订单超时自动取消（RabbitMQ 延迟队列）

### 社区服务 (sc-community)
- **社区公告** — 发布/查看/管理
- **访客登记** — 提交访客申请、管理员审核
- **车位管理** — 车位列表、车辆绑定/解绑
- **物业报修** — 提交报修、管理员受理/完成
- **投诉建议** — 提交投诉、管理员处理
- **物业缴费** — 账单生成、钱包支付

### 搜索服务 (sc-search)
- 基于 Elasticsearch 的商品全文搜索
- 关键词匹配商品名/副标题/描述
- 商品上下架自动同步索引

## 运行方式

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Elasticsearch 8.x
- RabbitMQ 3.x
- Node.js 18+（前端）

### 数据库初始化

创建 4 个业务数据库（表结构由 MyBatis-Plus 自动生成或手动建表）：

```sql
CREATE DATABASE sc_user    DEFAULT CHARSET utf8mb4;
CREATE DATABASE sc_mall    DEFAULT CHARSET utf8mb4;
CREATE DATABASE sc_trade   DEFAULT CHARSET utf8mb4;
CREATE DATABASE sc_community DEFAULT CHARSET utf8mb4;
```

### 启动后端服务

```bash
# 确保 MySQL / ES / RabbitMQ 已启动

# 方式一：使用启动脚本（Windows PowerShell）
.\start-final.ps1

# 方式二：手动依次启动
cd sc-gateway    && mvn spring-boot:run   # 8080
cd sc-auth       && mvn spring-boot:run   # 8100
cd sc-user       && mvn spring-boot:run   # 8200
cd sc-mall       && mvn spring-boot:run   # 8300
cd sc-trade      && mvn spring-boot:run   # 8400
cd sc-community  && mvn spring-boot:run   # 8500
cd sc-search     && mvn spring-boot:run   # 8600
```

### 启动前端

```bash
# 管理后台
cd sc-admin && npm install && npm run dev    # http://localhost:5173

# 居民端
cd sc-app && npm install && npm run dev      # http://localhost:5174
```

## 接口说明

所有接口通过 Gateway 统一访问 `http://localhost:8080`。

### 认证 `/api/auth`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/register` | 用户注册 |
| POST | `/api/auth/login` | 用户登录 |
| POST | `/api/auth/forgot-password` | 忘记密码 |

### 用户 `/api/user`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/user/profile` | 个人信息+钱包余额 |
| PUT | `/api/user/profile` | 修改个人信息 |
| PUT | `/api/user/password` | 修改密码 |
| GET | `/api/user/admin/list` | 后台用户列表 |
| PUT | `/api/user/admin/status` | 启用/禁用用户 |
| PUT | `/api/user/admin/roles` | 分配角色 |

### 商城 `/api/mall`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/mall/category/tree` | 分类树 |
| POST/PUT/DELETE | `/api/mall/category/{id}` | 分类 CRUD |
| GET | `/api/mall/product/page` | 商品分页查询 |
| POST/PUT/DELETE | `/api/mall/product/{id}` | 商品 CRUD |
| POST/PUT/DELETE | `/api/mall/store/{id}` | 店铺 CRUD |
| GET | `/api/mall/store-product/store/{storeId}` | 店铺商品列表 |

### 交易 `/api/trade`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/trade/cart/list` | 购物车列表 |
| POST | `/api/trade/cart/add` | 加入购物车 |
| PUT | `/api/trade/cart/{id}/quantity` | 修改数量 |
| DELETE | `/api/trade/cart/{id}` | 删除购物车项 |
| POST | `/api/trade/order/create` | 创建订单 |
| POST | `/api/trade/order/{id}/pay` | 钱包支付 |
| POST | `/api/trade/order/{id}/cancel` | 取消订单 |
| GET | `/api/trade/order/page` | 我的订单 |
| GET | `/api/trade/order/admin/page` | 后台订单管理 |
| POST | `/api/trade/order/{id}/deliver` | 发货 |
| POST | `/api/trade/order/{id}/finish` | 确认收货 |

### 社区 `/api/community`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/community/list` | 社区列表 |
| GET | `/api/community/notice/list` | 公告列表 |
| POST | `/api/community/visitor/submit` | 提交访客登记 |
| GET | `/api/community/visitor/my` | 我的访客记录 |
| POST | `/api/community/repair/submit` | 提交报修 |
| GET | `/api/community/repair/my` | 我的报修 |
| POST | `/api/community/complaint/submit` | 提交投诉 |
| GET | `/api/community/vehicle/my` | 我的车辆 |
| POST | `/api/community/vehicle/bind` | 绑定车辆 |
| GET | `/api/community/property-bill/my` | 我的物业账单 |
| POST | `/api/community/property-bill/{id}/pay` | 缴纳物业费 |

### 搜索 `/api/search`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/search?keyword=X` | 商品全文搜索 |

### 内部接口（不对外暴露）

| 方法 | 路径 | 调用方 | 说明 |
|------|------|------|------|
| POST | `/mall/internal/stock/lock` | sc-trade | 锁定库存 |
| POST | `/mall/internal/stock/unlock` | sc-trade | 释放库存 |
| POST | `/mall/internal/stock/confirm` | sc-trade | 确认扣减 |
| POST | `/user/internal/wallet/deduct` | sc-trade/comm | 钱包扣款 |
| POST | `/user/internal/wallet/refund` | sc-trade | 钱包退款 |
| POST | `/internal/product/index` | sc-mall | 索引商品至 ES |
| DELETE | `/internal/product/{id}` | sc-mall | 删除 ES 索引 |

## 数据库说明

| 数据库 | 服务 | 核心表 |
|------|------|------|
| `sc_user` | auth, user | uc_user, pay_wallet, sys_role, sys_user_role, pay_wallet_flow, sys_login_log |
| `sc_mall` | mall | mall_category, mall_product, mall_store, mall_store_product |
| `sc_trade` | trade | trade_cart_item, trade_order, trade_order_item, trade_order_status_log |
| `sc_community` | community | comm_community, comm_notice, comm_visitor, comm_parking_space, comm_vehicle, comm_repair, comm_complaint, comm_property_bill |

## 中间件依赖

| 中间件 | 用途 | 默认地址 |
|------|------|------|
| MySQL | 业务数据存储 | localhost:3306 |
| Elasticsearch | 商品全文检索 | localhost:9200 |
| RabbitMQ | 订单超时延迟消息 | localhost:5672 |
| Nacos | 服务注册发现（可禁用） | localhost:8848 |

## 相关文档

- [需求陈述书-东软智慧社区项目.docx](./需求陈述书-东软智慧社区项目.docx)
- [问题修复总结.md](./问题修复总结.md)
