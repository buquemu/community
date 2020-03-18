//公共方法
function comments(questionId, type, comment) {
    if (!comment) {
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        //将js对象转化为字符串
        data: JSON.stringify({
            "parentId": questionId,
            "content": comment,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                //刷新
                window.location.reload();
            } else {
                if (response.code == 102) {
                    //js对象window自带的能确定的框
                    var b = confirm(response.message);
                    if (b) {
                        window.open("https://github.com/login/oauth/authorize?client_id=e77dcf34f75f3d1de383&redirect_uri=http://localhost:8887/callback&scope=user&state=1")
                        //只能以字符串形式保存  会跳转到index开启一个新的页面 要保存一个这个来在index判断 是否删除
                        localStorage.setItem("close", true);
                    }
                } else {
                    alert(response.message)
                }
            }
        },
        dataType: "json"
    });

}

// 1级回复
function post() {
    //只有单个时可使用
    var questionId = $("#question_id").val();
    var comment = $("#question_comment").val();
    comments(questionId, 1, comment)
}

// 2级回复
function commentstwo(e) {
    var commentId = e.getAttribute("data-id");
    var comment = $("#input-" + commentId).val();
    comments(commentId, 2, comment)
}


//二级回复  样式
function erji(e) {
    var id = e.getAttribute("data-id");
    var comment = $("#comment-" + id);
    // 展开  折叠
    var dataopen = e.getAttribute("data-open");
    if (dataopen != null) {
        //折叠 二级评论
        comment.removeClass("in");
        e.removeAttribute("data-open");
        //展开效果  js添加class
        e.classList.remove("active")
    } else {
        //  获取到了ResultDTO  在页面展示 要里面的DATA
        var erjicomment = $("#comment-" + id);

        //修复多次展开 评论数增添多bug  根据子元素数量来判断
        if (erjicomment.children().length != 1) {
            //二级评论展开状态
            comment.addClass("in");
            e.setAttribute("data-open", "in");
            e.classList.add("active")
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data, function (index, comment) {
                    console.log(data);
                    var date = new Date(comment.gmtCreate);
                    Y = date.getFullYear() + '-';
                    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                    D = date.getDate() + ' ';
                    var time = Y + M + D

                    var mediaBody = $("<div/>", {
                        class: "media-body"
                    }).append($("<h6/>", {
                        class: "media-heading",
                        style: "margin-top: 4px;font-size: 14px"
                    }).append($("<span/>", {
                        style: "line-height: 35px;font-weight: 500;color: #801a1a",
                        html: comment.user.name
                    }))).append($("<div/>", {
                        html: comment.content,
                        style: "margin-top: 10px"
                    })).append($("<div/>", {
                        class: "pinglun",
                    }).append($("<span/>", {
                        html: time,
                        style: "float: right"
                    })));

                    //
                    var medialeft = $("<div/>", {
                        "class": "media-left",
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl,
                        style: "border 1px solid"
                    }));


                    var media = $("<div/>", {
                        "class": "media",
                    }).append(medialeft)
                        .append(mediaBody)
                    var c = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(media);
                    //追加到二级评论
                    erjicomment.prepend(c);
                });
                //二级评论展开状态
                comment.addClass("in");
                e.setAttribute("data-open", "in");
                e.classList.add("active")

            });
        }
    }
}

function dianzan(e) {
//    说明没点过攒
    //点击一下 增加
    //临时保存同一窗口的数据，在关闭窗口或标签页之后将会删除这些数据
    // var item = sessionStorage.getItem("key");
    var commentId = e.getAttribute("dianzan-id");
    //小手的哪个span标签
    var dangqian = $("#lailai-"+commentId);

    $.ajax({
        async:false,
        cache:false,
        type: "POST",
        url: "/praise/dianzan",
        contentType: 'application/json',
        data: JSON.stringify({
            "commentId": commentId,
        }),
        success: function (response) {
            if (response.code == 200) {//点赞成功
                    //通过后端查询返回给前端一个值  根据这个值判断该用户有没有给这个评论点过赞 如果点过就消除，如果没点过就增加
                //说明没点过赞
                if (response.status == 0) {
                    $.ajax({
                        async:false,
                        cache:false,
                        type: "POST",
                        url: "/comment/dianzan",
                        contentType: 'application/json',
                        data: JSON.stringify({
                            "commentId": commentId,
                            "type":1
                        }),
                        success: function (response) {
                            if (response.code == 200) {//点赞成功
                                //往里放值
                                dangqian.html(response.likeCount);
                            } else {
                                alert(response.message)
                            }
                        },
                        dataType: "json"
                    });
                } else {
                    $.ajax({
                        async:false,
                        cache:false,
                        type: "POST",
                        url: "/comment/dianzan",
                        contentType: 'application/json',
                        data: JSON.stringify({
                            "commentId": commentId,
                            "type":2
                        }),
                        success: function (response) {
                            if (response.code == 200) {//点赞成功
                                // 往里放值
                                dangqian.html(response.likeCount);
                            } else {
                                alert(response.message)
                            }
                        },
                        dataType: "json"
                    });
                    // //点一下删除
                    // sessionStorage.clear();
                }
            } else {
                alert(response.message)
            }
        },
        dataType: "json"
    });
}




function selectTag(e) {
    //将标签值放入input
    var previous = $("#tag").val();
    var value = e.getAttribute("data-tag")

    // 如果要检索的字符串值没有出现，则该方法返回 -1。
    if (previous.indexOf(value) == -1){
        if (previous) {
            $("#tag").val(previous + '，' + value);
        } else {
            $("#tag").val(value);
        }
    }
}


function showTag() {
     $('#showTag').slideDown(); //聚焦
}
function shijiao() {
    $('#showTag').slideUp();
}