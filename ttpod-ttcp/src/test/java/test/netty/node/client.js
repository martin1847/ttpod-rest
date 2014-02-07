var http = require("http");
var fs = require("fs");
//var ProtoBuf = require("protobufjs");
//var Message = ProtoBuf.protoFromFile("MyMessage.proto").build("Message");

var data = fs.readFileSync("d:\\test.txt");

function computeRawVarint32Size (value) {
    if ((value & (0xffffffff <<  7)) == 0) return 1;
    if ((value & (0xffffffff << 14)) == 0) return 2;
    if ((value & (0xffffffff << 21)) == 0) return 3;
    if ((value & (0xffffffff << 28)) == 0) return 4;
    return 5;
}

function writeRawVarint32(value) {
    var size = computeRawVarint32Size(value);
    var buffer = new Buffer(size);

    var i = 0;
    while (true) {
        if ((value & ~0x7F) == 0) {
            buffer.writeUInt8(value, i);
            break;
        } else {
            var temp = ((value & 0x7F) | 0x80);
            buffer.writeUInt8(temp, i);
            value >>>= 7;
        }
        i++;
    }
    return buffer;
}

function merge(a, b) {
    return Buffer.concat([a, b], a.length + b.length);
}

function formFrame(data) {
    var header = writeRawVarint32(data.length);
    return merge(header, data);
}

var net = require('net');

var socket = new net.Socket();

socket.connect(8080, "127.0.0.1");

socket.on('connect', function(){
    console.log("balala");
    //node.js在发送数据之前需要将二进制的数据转化一下，在前面添加一个采用Base 128 Varints编码的头部。。
    socket.write(formFrame(data));
});