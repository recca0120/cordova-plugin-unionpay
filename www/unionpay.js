var name = 'Unionpay';

var options = {
    payCallBackUrl: "https://payment.skillfully.com.tw/back.aspx",
    scode: '',
    key: ''
};

var Order = function(id, amount, memo) {
    this.id = id;
    this.amount = amount;
    this.memo = memo;
}

Order.prototype.setId = function(id) {
    this.id = id;
}

Order.prototype.setAmount = function(amount) {
    this.amount = amount;
}

Order.prototype.setMemo = function(memo) {
    this.memo = memo;
}

Order.prototype.request = function(success, fail) {
    if (!success) {
        success = function() {}
    }

    if (!fail) {
        fail = function() {}
    }

    cordova.exec(success, fail, name, "payOrderRequest", [
        this.id,
        this.amount,
        this.memo,
        options.payCallBackUrl,
        options.scode,
        options.key,
    ]);
}


module.exports = {
    set: function (key, value) {
        options[key] = value;
    },
    Order: Order
}
