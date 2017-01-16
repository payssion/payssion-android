# PayssionSDK
## 安装

**由于Google已放弃支持ADT Bundle(adt-bundle-android-developer-tools)，详情[点击](https://android-developers.googleblog.com/2015/06/an-update-on-eclipse-android-developer.html)。该SDK也不再支持ADT Bundle，建议开发者转向Android Studio。项目迁移请点击[从 Eclipse 迁移至 Android Studio](https://developer.android.com/studio/intro/migrate.html?hl=zh-cn#android_studio),Android Studio下载请点击[官方地址](https://developer.android.com/studio/index.html?hl=zh-cn)，免代理请[点击这里](https://pan.baidu.com/s/1o8bKpvW)。Android Studio上手使用请点击[探索 Android Studio](https://developer.android.com/studio/intro/index.html?hl=zh-cn)。由于历史原因不便迁移的请参考以下条目。**

- **ADT Bundle**

   使用ADT Bundle的开发者请参考[本文档](/adt-bundle.md)

- **Eclipse + ADT plugin + sdk**
   
   使用Eclipse + ADT plugin + sdk的开发者请参考[本文档](/eclipse.md)

- **Android Studio**

   使用Android Studio的开发者请继续阅读。

##### 1. 将文件`PayssionSDK.jar`拷贝到`libs/`文件夹

配置app级别build.gradle,在dependencies中增加以下compile
 - compile 'com.android.support:appcompat-v7:xx.xx'
 - compile files('libs/PayssionSDK.jar')

##### 2. 在`AndroidManifest.xml`文件中添加以下权限:
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
**以及以下Activity声明**
```xml
<activity
    android:name="com.payssion.android.sdk.PayssionActivity"
    android:label="@string/app_name"
    android:theme="@style/Theme.PayssionTrans"/>
<activity
    android:name="com.payssion.android.sdk.PaymentWebActivity"
    android:label="@string/app_name"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar"/>
```

##### 3. 在values/styles.xml文件中添加以下style
```xml
   <style name="Theme.PayssionTrans" parent="Theme.AppCompat.Light">
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="android:windowNoTitle">true</item>
        <item name="android:windowIsFloating">true</item>
        <item name="android:windowFullscreen">true</item>
        <item name="android:backgroundDimEnabled">true</item>
    </style>
```
## 参数说明

| 方法名  	      | 参数名         | 类型         | 必填          | 示例          | 释义          | 详解         |
| :------------ | :------------ |:------------ |:--- |:--------------- |:---------------- |:--------------------- |
| setLiveMode   | live_mode     | boolean      | 否           | true/false    | 程序运行环境 |测试环境传false.正式环境传true.默认正式环境
| setAPIKey     | api_key       | string       | 是           | 5963a4c1c35c2a8e  | App id    | 注意区分对应环境的api_key
| setSecretKey  | secret_key    | string       | 是           | demo456           | 密钥       | 注意区分对应环境的secret_key
| setOrderId    | order_id      | string       | 是           | 1989093-251658248 | 订单id     | 需自定义，且长度不超过32位
| setAmount     | amount        | double       | 是           | 1.99              | 订单总金额  | 
| setCurrency   | currency      | string       | 是           | USD、CNY等         | 货币种类    | 大写，币种缩写可搜索公共信息
| setPMId       | pmid          | string       | 是           | sofort             | 支付方式id | [供参考](https://payssion.com/en/docs/#api-reference-pm-id)。具体询问我司商务经理 
| setDescription| description   | string       | 是           | game recharge #123 | 订单描述 | 
| setPayerEmail | payer_email   | string       | 是           | example@demo.com   | 付款方邮箱 | 南美地区支付必须填写邮箱。具体询问我司商务经理
| setPayerName  | payer_name    | string       | 否           | example name       | 付款方姓名 | 
| setPayerRef   | payer_ref     | string       | 否           | example ref        | 付款方其他信息 | 

## 使用
###创建交易
在`MainActivity`通过以下代码创建交易
```java
Intent intent = new Intent(MainActivity.this, PayssionActivity.class);
intent.putExtra(PayssionActivity.ACTION_REQUEST,
        new PayRequest()
        .setLiveMode(false) //测试环境传false.正式环境传true.默认正式环境
        .setAPIKey(apiKey) //请注意区分测试环境和正式环境的APIKey
        .setAmount(amount)
        .setCurrency(currency)
        .setPMId(pmId)
        .setDescription(description)
        .setOrderId(orderId) //您的订单Id
        .setSecretKey(secreKey)//请注意区分测试环境和正式环境的SecretKey
        .setPayerEmail(payerEmail)
        .setPayerName(payerName));
MainActivity.this.startActivityForResult(intent, 0);
```
###处理返回数据
需要在`MainActivity`重写`onActivityResult`函数
```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(this.getClass().getSimpleName(), "onActivityResult");
        switch (resultCode) {
        case PayssionActivity.RESULT_OK:
            if (null != data) {
                PayResponse response = (PayResponse)data.getSerializableExtra(PayssionActivity.RESULT_DATA);
                if (null != response) {
                    String transId = response.getTransactionId(); //获取Payssion交易Id
					String orderId = response.getOrderId(); //获取您的订单Id
                    //you will have to query the payment state with the transId or orderId from your server
                    //as we will notify you server whenever there is a payment state change
                } else {
                    //should never go here
                }
            }
            break;
        case PayssionActivity.RESULT_CANCELED:
            //the transation has been cancelled, for example, the users doesn't pay but get back
            break;
        case PayssionActivity.RESULT_ERROR:
            //there is some error
            if (null != data) {
                String err_des = data.getStringExtra(PayssionActivity.RESULT_DESCRIPTION);
                Log.v(this.getClass().getSimpleName(), "RESULT_ERROR" + err_des);   
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
```

## 返回数据说明

###1.参数说明

返回数据封装为PayResponse,通过Intent.getSerializableExtra(PayssionActivity.RESULT_DATA)方法获取

| 方法名  	      | 参数名         | 类型          | 释义              | 详解                   |
| :------------ | :------------ |:------------ |:----------- |:------------------------- |
| getTransactionId   | transaction_id     | String      | 交易id|Payssion 交易号 id,如订单异常,需提供此 id
| getOrderId   | order_id     | String      | 订单id| 客户发起支付时自定义的订单 id。注意与`transaction_id`区分
| getState| state     | String      | 支付状态| 支付完成状态，准确支付结果以notify_url通知为准
| getAmount| amount     | String      | 订单金额|

###2.ResultCode说明

| 参数名  	        | 释义       | 类型 | 值
| :-------------- | :--------- | :--------- | :--------- |
| RESULT_OK       | 支付成功    | int| 770
| RESULT_CANCELED | 支付取消    | int| 771
| RESULT_ERROR    | 支付异常    | int| 772


###3.注意事项
1. 需要注意的是这里的支付成功意味着支付流程成功，因为银行间结算有延迟，所以最终的支付结果要以您后台配置的[`otify_url`](https://payssion.com/en/docs/#api-reference-payment-notifications)([中文请点击](https://payssion.com/cn/docs/#api-notifications))收到的通知为准。

2. 在接收`notify_url`数据时需验证签名`notify_sig`。详情请[查阅文档](https://payssion.com/en/docs/#api-reference-signature)([中文请点击](https://payssion.com/cn/docs/#api-api-signature))以及[Demo](https://github.com/payssion/payssion-php/blob/master/samples/sample_postback.php)。

##PMID设置
##### 1. 您可以设置仅支持`一种支付方式`.  
在创建交易的`PayRequest`中设置  

**例:**
```java
Intent intent = new Intent(MainActivity.this, PayssionActivity.class);
intent.putExtra(PayssionActivity.ACTION_REQUEST,
new PayRequest()
    .setLiveMode(false)
    .setAPIKey(apiKey)
    .setAmount(amount)
    .setCurrency(currency)
    .setPMId(pmId)
    ...
MainActivity.this.startActivityForResult(intent, 0);
```
##### 2. 您可以设置支持`多种支付方式`.  
我们提供以下两种函数支持您合理配置所需的支付方式.  
您可以使用我们提供的支付方式`logo`,也可以在您工程的`assets`文件夹创建`”payssion/pm/“`路径,并将您自定义的支付方式`logo图片`放到该路径下.  
图片格式默认为`.png`,图片名称为该支付方式的`PMID`.
```java
   /**
     * 设置需要支持的支付方式
     * @param enablePM 需支持的PMID.如需多个可用“|”分割开。比如"pm_idA|pm_idB|pm_idC"
     */
    PayssionConfig.enablePM(String enablePM);

   /**
     * 设置不需要支持的支付方式
     * @param disEnablePM 不需支持的PMID.如需多个可用“|”分割开。比如"pm_idA|pm_idB|pm_idC"
     */
    PayssionConfig.disenablePM(String disenablePM);
```
## 设置主题颜色
您可以通过以下函数设置Theme颜色.
```java
   /**
     * Set Theme Color
     * @param color theme color
     */
PayssionConfig.setThemeColor(@ColorInt int color);
```
默认颜色是通过读取
```java
getTheme().resolveAttribute(android.R.attr.colorAccent, TypedValue, true);  
defaultColor = TypedValue.data;
```
获得.

**\*注意*** PayssionConfig相关的函数需要在创建交易之前设置  

## 多语言设置
我们支持汉语简体(ZH_SIMPLIFIED)、汉语繁体(ZH_TRADITIONAL)、英语(EN)、德语(DE)、西班牙语(ES)、葡萄牙语(PT)、俄语(RU)、阿拉伯语(AR)等语种.  
您可以通过`PayRequest.setLanguage(String language)`函数或者`PayssionConfig.setLanguage(String language)`设置语言,传入参数为`**PLanguage**`类的语种变量.  

**例:**  
```java
PayRequest payRequest = new PayRequest();
payRequest.setLanguage(PLanguage.ZH_SIMPLIFIED);
...
```
或
```java
PayssionConfig.setLanguage(PLanguage.ZH_SIMPLIFIED);
...
```

默认语种会通过`java.util.Locale.getDefault().getLanguage()`函数读取本地语言设置.

**\*注意***  
1. PayRequest设置语言的优先级要大于PayssionConfig.  
2. 阿拉伯语需要在`AndroidManifest.xml`文件中的`application`元素中加入`android:supportsRtl="true"`属性.
    该属性支持Android4.2及以上版本.  
    
**例:**  
```xml
<application
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:supportsRtl="true"
    android:theme="@style/AppTheme">
    ...
</application>
```

