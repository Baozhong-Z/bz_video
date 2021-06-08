$(function () {
    $("body").append('<div class="mobile-get-top" id="return-top" style="display: block;"></div>');
    $(".mobile-get-top").click(function () {
        $('html,body').animate({'scrollTop': 0}, 500);
    });
    $(".mobile-get-top").hide();
    $(window).scroll(function () {
        //获得滑轮滚动的距离
        if($(window).scrollTop() >= 300){
            $("#return-top").show(100);
        }else{
            $("#return-top").hide(100);
        }
    });
});
function logout() {
    var b = confirm("确定退出登录吗？");
    if (b){
        location.href="/logout";
    }
}