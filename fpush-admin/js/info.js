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
            app.dialogFormVisible = false;
            axios.post(ctxPath + '/app/push', app.pushForm).then(function (response) {
                console.log(response);
                this.$message({
                    message: '恭喜你，这是一条成功消息',
                    type: 'success',
                    center: true
                });
            }).catch(function (error) {
                console.log(error);
            });
        }
    }
});

app.getTable();

//# sourceMappingURL=info.js.map