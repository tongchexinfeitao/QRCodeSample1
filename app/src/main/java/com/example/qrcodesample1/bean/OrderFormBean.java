package com.example.qrcodesample1.bean;

import java.util.List;

public class OrderFormBean {

    /**
     * orderList : [{"detailList":[{"commentStatus":1,"commodityCount":2,"commodityId":5,"commodityName":"双头两用修容笔","commodityPic":"http://172.17.8.100/images/small/commodity/mzhf/cz/3/1.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/2.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/3.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/4.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/5.jpg,","commodityPrice":39.1,"orderDetailId":18418},{"commentStatus":1,"commodityCount":1,"commodityId":11,"commodityName":"盒装葫芦粉扑美妆蛋化妆海绵","commodityPic":"http://172.17.8.100/images/small/commodity/mzhf/mzgj/1/1.jpg,http://172.17.8.100/images/small/commodity/mzhf/mzgj/1/2.jpg,http://172.17.8.100/images/small/commodity/mzhf/mzgj/1/3.jpg,http://172.17.8.100/images/small/commodity/mzhf/mzgj/1/4.jpg,http://172.17.8.100/images/small/commodity/mzhf/mzgj/1/5.jpg","commodityPrice":9,"orderDetailId":18419}],"expressCompName":"京东快递","expressSn":"1001","orderId":"201909091622349558112","orderStatus":2,"orderTime":1568017355000,"payAmount":87,"payMethod":1,"userId":8112},{"detailList":[{"commentStatus":1,"commodityCount":1,"commodityId":5,"commodityName":"双头两用修容笔","commodityPic":"http://172.17.8.100/images/small/commodity/mzhf/cz/3/1.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/2.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/3.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/4.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/5.jpg,","commodityPrice":39,"orderDetailId":16839}],"expressCompName":"京东快递","expressSn":"1001","orderId":"201908151652019718112","orderStatus":2,"orderTime":1565859122000,"payAmount":39,"payMethod":1,"userId":8112}]
     * message : 查询成功
     * status : 0000
     */

    private String message;
    private String status;
    private List<OrderListBean> orderList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean {
        /**
         * detailList : [{"commentStatus":1,"commodityCount":2,"commodityId":5,"commodityName":"双头两用修容笔","commodityPic":"http://172.17.8.100/images/small/commodity/mzhf/cz/3/1.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/2.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/3.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/4.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/5.jpg,","commodityPrice":39.1,"orderDetailId":18418},{"commentStatus":1,"commodityCount":1,"commodityId":11,"commodityName":"盒装葫芦粉扑美妆蛋化妆海绵","commodityPic":"http://172.17.8.100/images/small/commodity/mzhf/mzgj/1/1.jpg,http://172.17.8.100/images/small/commodity/mzhf/mzgj/1/2.jpg,http://172.17.8.100/images/small/commodity/mzhf/mzgj/1/3.jpg,http://172.17.8.100/images/small/commodity/mzhf/mzgj/1/4.jpg,http://172.17.8.100/images/small/commodity/mzhf/mzgj/1/5.jpg","commodityPrice":9,"orderDetailId":18419}]
         * expressCompName : 京东快递
         * expressSn : 1001
         * orderId : 201909091622349558112
         * orderStatus : 2
         * orderTime : 1568017355000
         * payAmount : 87
         * payMethod : 1
         * userId : 8112
         */

        private String expressCompName;
        private String expressSn;
        private String orderId;
        private int orderStatus;
        private long orderTime;
        private int payAmount;
        private int payMethod;
        private int userId;
        private List<DetailListBean> detailList;

        public String getExpressCompName() {
            return expressCompName;
        }

        public void setExpressCompName(String expressCompName) {
            this.expressCompName = expressCompName;
        }

        public String getExpressSn() {
            return expressSn;
        }

        public void setExpressSn(String expressSn) {
            this.expressSn = expressSn;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public long getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(long orderTime) {
            this.orderTime = orderTime;
        }

        public int getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(int payAmount) {
            this.payAmount = payAmount;
        }

        public int getPayMethod() {
            return payMethod;
        }

        public void setPayMethod(int payMethod) {
            this.payMethod = payMethod;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public List<DetailListBean> getDetailList() {
            return detailList;
        }

        public void setDetailList(List<DetailListBean> detailList) {
            this.detailList = detailList;
        }

        public static class DetailListBean {
            /**
             * commentStatus : 1
             * commodityCount : 2
             * commodityId : 5
             * commodityName : 双头两用修容笔
             * commodityPic : http://172.17.8.100/images/small/commodity/mzhf/cz/3/1.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/2.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/3.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/4.jpg,http://172.17.8.100/images/small/commodity/mzhf/cz/3/5.jpg,
             * commodityPrice : 39.1
             * orderDetailId : 18418
             */

            private int commentStatus;
            private int commodityCount;
            private int commodityId;
            private String commodityName;
            private String commodityPic;
            private double commodityPrice;
            private int orderDetailId;

            public int getCommentStatus() {
                return commentStatus;
            }

            public void setCommentStatus(int commentStatus) {
                this.commentStatus = commentStatus;
            }

            public int getCommodityCount() {
                return commodityCount;
            }

            public void setCommodityCount(int commodityCount) {
                this.commodityCount = commodityCount;
            }

            public int getCommodityId() {
                return commodityId;
            }

            public void setCommodityId(int commodityId) {
                this.commodityId = commodityId;
            }

            public String getCommodityName() {
                return commodityName;
            }

            public void setCommodityName(String commodityName) {
                this.commodityName = commodityName;
            }

            public String getCommodityPic() {
                return commodityPic;
            }

            public void setCommodityPic(String commodityPic) {
                this.commodityPic = commodityPic;
            }

            public double getCommodityPrice() {
                return commodityPrice;
            }

            public void setCommodityPrice(double commodityPrice) {
                this.commodityPrice = commodityPrice;
            }

            public int getOrderDetailId() {
                return orderDetailId;
            }

            public void setOrderDetailId(int orderDetailId) {
                this.orderDetailId = orderDetailId;
            }
        }
    }
}
