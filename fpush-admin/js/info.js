'use strict';

var app = new Vue({
    el: '#app',
    data: function data() {
        return {
            tableData: [],
            dialogFormVisible: false,
            pushForm: {
                receiverAlias: '',
                title: '',
                desc: '',
                data: ''
            },
            formLabelWidth: '120px'
        };
    },
    methods: {
        openPushDialog: function openPushDialog(index, row) {
            console.log(index, row);
            app.pushForm.receiverAlias = row.alias;
            app.dialogFormVisible = true;

        },
        handleDelete: function handleDelete(index, row) {
            console.log(index, row);
        },
        getTable: function getTable() {
            axios.post(ctxPath + '/monitor/channelList', null).then(function (response) {
                console.log(response);
                app.tableData = response.data;
            }).catch(function (error) {
                console.log(error);
            });
        },
        push: function push() {
            var pass = validatePushForm();
            if (pass) {
                app.dialogFormVisible = false;
                axios.post(ctxPath + '/app/push', app.pushForm).then(function (response) {
                    console.log(response);
                    app.$message({
                        message: '发送即时消息成功',
                        type: 'success',
                        center: true
                    });
                }).catch(function (error) {
                    console.log(error);
                });
            }
        }
    }
});

function validatePushForm() {
    var pass = true;
    var errorMsg = "";

    app.pushForm.receiverAlias

    if (app.pushForm.desc == undefined || app.pushForm.desc == "") {
        pass = false;
        errorMsg = "文本内容为空";
    }

    if (app.pushForm.title == undefined || app.pushForm.title == "") {
        pass = false;
        errorMsg = "标题为空";
    }

    if (app.pushForm.receiverAlias == undefined || app.pushForm.receiverAlias == "") {
        pass = false;
        errorMsg = "接收用户名为空";
    }



    if (!pass) {
        app.$message({
            message: errorMsg,
            type: 'error',
            center: true
        });
    }

    return pass;
}

app.getTable();

//# sourceMappingURL=info.js.map