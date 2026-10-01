# easyjava

一个基于数据库表结构反向生成 Java 代码的代码生成器（逆向工程）示例项目。

读取 MySQL 中的表结构，自动生成 Spring Boot + MyBatis 的
`PO / Query / Mapper / Mapper.xml / Service / ServiceImpl / Controller` 等模板代码。

## 模块说明

| 模块 | 说明 |
| --- | --- |
| `easyjava` | 代码生成器本体。读取 `src/main/resources/template/` 下的模板，按表结构生成代码并输出到指定目录。 |
| `easyjava-demo` | 由生成器生成的示例工程（Spring Boot + MyBatis），用于演示生成结果与 CRUD 测试。 |

## 环境要求

- JDK 8
- Maven 3.x
- MySQL 8.x（本地可用）

## 快速开始

### 1. 准备配置

仓库中不包含真实的数据库配置，请基于示例文件自行创建：

```bash
cp easyjava/src/main/resources/application.properties.example      easyjava/src/main/resources/application.properties
cp easyjava-demo/src/main/resources/application.properties.example  easyjava-demo/src/main/resources/application.properties
```

然后填写你自己的数据库地址、用户名和密码，例如 `easyjava` 模块中的：

```properties
db.url=jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=GMT%2B8
db.username=your_username
db.password=your_password
# 生成代码的输出根目录，需以 /src/main/ 结尾
path.base = /path/to/easyjava-demo/src/main/
# 生成代码的 @author 署名
comment.author=YourName
```

> `application.properties` 已被 `.gitignore` 忽略，不会被提交。

### 2. 运行代码生成器

在 `easyjava` 模块中运行 `com.easyjava.RunApplication` 的 `main` 方法，
生成代码会输出到 `path.base` 指定的目录。

### 3. 运行示例工程

在 `easyjava-demo` 模块中运行 `com.easyjava.RunDemoApplication`，
默认端口 `8070`。

## 目录结构

```
easyjava/
├── easyjava/          # 代码生成器
│   └── src/main/resources/template/   # 代码模板
├── easyjava-demo/     # 生成结果示例工程
└── README.md
```

## License

[MIT](LICENSE)
