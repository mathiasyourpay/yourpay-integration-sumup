// Empty constructor
function SumupPlugin() {}

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
SumupPlugin.prototype.payment = function(amount, currency, device, payment_text, transaction_identifier, merchantIdentifier, merchantSecretkey, successCallback, errorCallback) {
    var options = {};
    options.method = "payment";
    options.device_type = device;
    options.amount = amount;
    options.currency = currency;
    options.payment_text = payment_text;
    options.transaction_identifier = transaction_identifier;
    options.production = 1;
    options.merchant_identifier = merchantIdentifier;
    options.merchant_secret_key = merchantSecretkey;
    cordova.exec(successCallback, errorCallback, 'SumupPlugin', 'payment', [options]);
}

SumupPlugin.prototype.refund = function(amount, currency, device, payment_text, transaction_identifier, merchantIdentifier, merchantSecretkey, successCallback, errorCallback) {
    var options = {};
    options.method = "refund";
    options.device_type = device;
    options.amount = amount;
    options.currency = currency;
    options.payment_text = payment_text;
    options.transaction_identifier = transaction_identifier;
    options.production = 1;
    options.merchant_identifier = merchantIdentifier;
    options.merchant_secret_key = merchantSecretkey;
    cordova.exec(successCallback, errorCallback, 'SumupPlugin', 'refund', [options]);
}

SumupPlugin.install = function() {
    if (!window.plugins) {
        window.plugins = {};
    }
    window.plugins.SumupPlugin = new SumupPlugin();
    return window.plugins.SumupPlugin;
};
cordova.addConstructor(SumupPlugin.install);