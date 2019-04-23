
[![](https://jitpack.io/v/kongqw/PermissionsManager.svg)](https://jitpack.io/#kongqw/PermissionsManager)

## 部署

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

``` gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency

``` gradle
dependencies {
        implementation 'com.github.kongqw:PermissionsManager:2.1.0'
}
```


## 应用

使用起来的逻辑也比较清晰简单：

### 1. 在Application中初始化权限管理器

``` java
// 初始化动态权限管理器
XPermissionsManager.init(this)
```

### 2. 请求权限

``` java
// 请求权限
XPermissionsManager.checkPermissions(`请求码`, `请求权限`, `回调监听`)
```


进入应用设置页面

最后，权限没有通过，是不能使用的，如果一定要用，一般要提示用户缺少权限，到应用设置页面去把权限打开，再回来使用。
对话框就不写了，进入到应用的设置页面可以直接调用`PermissionsManager`里的`startAppSettings`方法，进入到该应用的设置页面，修改权限

``` java
PermissionsManager.startAppSettings(getApplicationContext());
```

## 关于Android 6.0 以下版本的权限校验

动态检查权限是Android 6.0 以后添加的权限检查方案。但是在6.0之前的版本，只要在清单文件里添加里权限，即使在设置中拒绝了权限，校验结果也是通过，校验没有没意义。

由于手机厂商对ROM都有定制，如果一定要校验，可以查看对应ROM有没有给出API，但是极为碎片化，不建议。

最好的解决方案就是根据对应功能是否达到了预期效果来判断，如果没有达到预期效果，那么可能是没有权限。
