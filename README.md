
# PayssionSDK

[中文文档](https://github.com/payssion/payssion-android/blob/master/README_ZH.md)

[FAQ中文](https://github.com/payssion/payssion-android/blob/master/FAQ_ZH.md)

## Installation
##### 1. Copy `PayssionSDK.jar` to the `libs/` folder.

`Android Studio`：

Add the following to your `build.gradle`:
 - compile 'com.android.support:appcompat-v7:xx.xx'
 - compile files('libs/PayssionSDK.jar')

`Eclipse`：

 - Import android-support-v7-appcompat library.
 - `PayssionSDK.jar`Add to Build Path.    

##### 2. Make sure you have added the following permission to the `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
**and This Activity**
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

##### 3. Add the following to your `values/styles.xml`
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
## Usage
### Create a transaction
Let's say you create a transaction in `MainActivity`.
```java
Intent intent = new Intent(MainActivity.this, PayssionActivity.class);
intent.putExtra(PayssionActivity.ACTION_REQUEST,
        new PayRequest()
        .setLiveMode(false) //false if you are using sandbox environment
        .setAPIKey(apiKey) //Your API Key
        .setAmount(amount)
        .setCurrency(currency)
        .setPMId(pmId)
        .setDescription(description)
        .setOrderId(orderId) //Your order id
        .setSecretKey(secreKey)
        .setPayerEmail(payerEmail)
        .setPayerName(payerName));
MainActivity.this.startActivityForResult(intent, 0);
```
###Handle result
You need to overide `onActivityResult` method in `MainActivity`.
```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(this.getClass().getSimpleName(), "onActivityResult");
        switch (resultCode) {
        case PayssionActivity.RESULT_OK:
            if (null != data) {
                PayResponse response = (PayResponse)data.getSerializableExtra(PayssionActivity.RESULT_DATA);
                if (null != response) {
                    String transId = response.getTransactionId(); //get Payssion transaction id
					String orderId = response.getOrderId(); //get your order id
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
##PMIDSetting
##### 1. You can set supports only one payment for `PayRequest`   

**Example:**
```java
Intent intent = new Intent(MainActivity.this, PayssionActivity.class);
intent.putExtra(PayssionActivity.ACTION_REQUEST,
new PayRequest()
    .setLiveMode(false) //false if you are using sandbox environment
    .setAPIKey(apiKey) //Your API Key
    .setAmount(amount)
    .setCurrency(currency)
    .setPMId(pmId)
    ...
MainActivity.this.startActivityForResult(intent, 0);
```
##### 2. You can set up support for multiple payment methods.  
We provide the following two functions to support you in a reasonable configuration of the required payment methods.  
You can use the payment method we provide `logo`, you can also project the `assets` folder to create the `"payssion/pm/"` path, and you will be able to customize the payment method logo pictures under the path.
Image format is`.png`, the picture names for the payment method of `PMID`.
```java
   /**
     * set enable payment methods
     * @param enablePM enable PMID.For more than one can be used "to" split open。For example"pm_idA|pm_idB|pm_idC"
     */
    PayssionConfig.enablePM(String enablePM);

   /**
     * set disable payment methods
     * @param disablePM disable PMID.For more than one can be used "to" split open。For example"pm_idA|pm_idB|pm_idC"
     */
    PayssionConfig.disablePM(String disenablePM);
```
### Set Theme Color
You can set theme color following this.
```java
   /**
     * Set Theme Color
     * @param color theme color
     */
PayssionConfig.setThemeColor(@ColorInt int color);
```
Color default reads the following code to obtain 
```java
getTheme().resolveAttribute(android.R.attr.colorAccent, TypedValue, true);  
defaultColor = TypedValue.data;
```

**\*Tips*** PayssionConfig's function needs to be called before the transaction is created.  

### Multi language setting
We support the Chinese Simplified (ZH_SIMPLIFIED), Traditional Chinese (ZH_TRADITIONAL), English (EN), German (DE), Spanish (ES), Portuguese (PT), Russian (RU), Arabic (AR) and other languages.  
You can set the language through the function`PayRequest.setLanguage(String language)`or the function`PayssionConfig.setLanguage(String language)`,the incoming parameter is the language variable of the`**PLanguage**`class.  

**Example:**  
```java
PayRequest payRequest = new PayRequest();
payRequest.setLanguage(PLanguage.ZH_SIMPLIFIED);
...
```
or
```java
PayssionConfig.setLanguage(PLanguage.ZH_SIMPLIFIED);
...
```

The default language can be read by the`java.util.Locale.getDefault().getLanguage()`function in the local language settings.

**\*Tips***  
1. PayRequest sets the priority of the language to be greater than PayssionConfig.  
2. Arabic needs to add the `android:supportsRtl= "true" attribute to the `application` element in the `AndroidManifest.xml` file.
    This property supports Android4.2 and above versions.  
    
**Example:**  
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
    
