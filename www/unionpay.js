(function(){var t,n,o;n="Unionpay",o={payCallBackUrl:"https://payment.skillfully.com.tw/back.aspx"},t=function(){function t(t,n,o){this.id=null!=t?t:null,this.amount=null!=n?n:null,this.memo=null!=o?o:null}return t.prototype.setId=function(t){this.id=t},t.prototype.setAmount=function(t){this.amount=t},t.prototype.setMemo=function(t){this.memo=t},t.prototype.request=function(t,e){return e||(e=function(t){return console.log(t)}),cordova.exec(t,e,n,"payOrderRequest",[this.id,this.amount,this.memo,o.payCallBackUrl])},t}(),module.exports={set:function(t,n){return o[t]=n},Order:t}}).call(this);
