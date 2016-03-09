name = "Unionpay"
options =
    payCallBackUrl: "https://payment.skillfully.com.tw/back.aspx"

class Order
    constructor: (@id = null, @amount = null, @memo = null) ->
    setId: (@id) ->
    setAmount: (@amount) ->
    setMemo: (@memo) ->

    request: (success, fail) ->
        unless fail
            fail = (_err) ->
                console.log _err

        cordova.exec success, fail, name, "payOrderRequest", [
            @id
            @amount
            @memo
            options.payCallBackUrl
        ]

module.exports =
    set: (key, value) ->
        options[key] = value
    Order: Order
