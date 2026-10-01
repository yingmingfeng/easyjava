# easyjava

**基于 MySQL 表结构反向生成 Spring Boot + MyBatis 代码的代码生成器。**

读取数据库中的表结构，自动生成 `PO / Query / Mapper / Mapper.xml / Service / ServiceImpl / Controller`
等基础代码，省去手写重复的 CRUD 样板代码。

![Java](https://img.shields.io/badge/Java-8-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.1-brightgreen)
![MyBatis](https://img.shields.io/badge/MyBatis-1.3.2-red)
![License](https://img.shields.io/badge/License-MIT-blue)

## 模块说明

| 模块 | 说明 |
| --- | --- |
| `easyjava` | 代码生成器本体。读取 `src/main/resources/template/` 下的模板，按表结构生成代码并输出到指定目录。 |
| `easyjava-demo` | 由生成器生成的示例工程（Spring Boot + MyBatis），用于演示生成结果与 CRUD 测试。 |

## 环境要求

- JDK 8
- Maven 3.x
- MySQL 8.x（本地可用）

## 使用方法

### 1. 准备配置

仓库中**不包含真实的数据库配置**，请基于示例文件自行创建：

```bash
cp easyjava/src/main/resources/application.properties.example      easyjava/src/main/resources/application.properties
cp easyjava-demo/src/main/resources/application.properties.example  easyjava-demo/src/main/resources/application.properties
```

> `application.properties` 已被 `.gitignore` 忽略，不会被提交。

### 2. 修改数据库配置

在 `easyjava/src/main/resources/application.properties` 中填写你自己的数据库信息：

```properties
db.url=jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=GMT%2B8
db.username=your_username
db.password=your_password
```

同时修改生成参数：

```properties
# 生成代码的输出根目录，需以 /src/main/ 结尾
path.base = /path/to/easyjava-demo/src/main/
# 生成代码的 @author 署名
comment.author=YourName
```

> ⚠️ `path.base` 指向的目录会在生成时**被覆盖**。若目录中已有手工修改过的代码，请先备份，
> 或改为生成到一个新目录。

### 3. 运行代码生成器

在 `easyjava` 模块中运行 `com.easyjava.RunApplication` 的 `main` 方法，
生成代码会输出到 `path.base` 指定的目录。

### 4. 运行示例工程

在 `easyjava-demo` 模块中运行 `com.easyjava.RunDemoApplication`，默认端口 `8070`。
建议先跑通示例工程确认无误，再生成自己的项目。

## 目录结构

```
easyjava/
├── easyjava/                          # 代码生成器
│   └── src/main
│       ├── java/com/easyjava
│       │   ├── RunApplication.java    # 运行主类，点击运行即可生成 MyBatis 基础项目
│       │   ├── bean                   # Constants/FieldInfo/TableInfo 等数据结构
│       │   ├── builder                # BuildBase/BuildPo/BuildMapper... 各类构建器
│       │   └── utils                  # DateUtils/JsonUtils/PropertiesUtils/StringUtils
│       └── resources
│           ├── application.properties.example   # 配置示例（真实配置请自行创建）
│           └── template               # 固定模板类（A*.txt / Base*.txt 等）
└── easyjava-demo/                     # 生成结果示例工程
    └── src/main
        ├── java/com/easyjava          # controller / entity / mappers / service / utils
        └── resources
            ├── application.properties.example
            └── com/easyjava/mappers/ProductInfoMapper.xml
```

## License

[MIT](LICENSE)
