// Empty constructor
function SumupPlugin() {}

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'

SumupPlugin.prototype.login = function(successCallback,errorCallback){
    var options = {};
    options.method = "login";
    cordova.exec(successCallback, errorCallback, 'SumupPlugin', "login", [options])
}
SumupPlugin.prototype.pay = function(successCallback,errorCallback){
    var options = {};
    options.amount=200;
    options.currency="DKK";
    options.email="msp@yourpay.io";
    options.tel ="70555678";
    cordova.exec(successCallback, errorCallback, 'SumupPlugin', "pay", [8,"DKK","msp@yourpay.io",70555678])
}
SumupPlugin.prototype.payWithToken = function(successCallback,errorCallback){
    var options = {};
    options.method = "login";
    cordova.exec(successCallback, errorCallback, 'SumupPlugin', "payWithToken", [options])
}
SumupPlugin.prototype.settings = function(successCallback,errorCallback){
    var options = {};
    options.method = "login";
    cordova.exec(successCallback, errorCallback, 'SumupPlugin', "settings", [options])
}
SumupPlugin.prototype.logout = function(successCallback,errorCallback){
    var options = {};
    options.method = "login";
    cordova.exec(successCallback, errorCallback, 'SumupPlugin', "logout", [options])
}

SumupPlugin.install = function() {
    if (!window.plugins) {
        window.plugins = {};
    }
    window.plugins.SumupPlugin = new SumupPlugin();
    return window.plugins.SumupPlugin;
};
cordova.addConstructor(SumupPlugin.install);