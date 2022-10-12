$(document).ready(function () {
  // 헤더 스크롤 처리
  if ($(window).scrollTop() >= 64) {
    $("#header").addClass("sticky");
  }

  // 헤더 스크롤 이벤트
  $(window).scroll(function () {
    var scrollTop = $(this).scrollTop();
    if (scrollTop >= 64) {
      $("#header").addClass("sticky");
    } else {
      $("#header").removeClass("sticky");
    }
  });

  // 드롭다운
  $(".dropdown-toggle").click(function () {
    $(this).next(".dropdown").slideToggle();
  });

  $(document).click(function (e) {
    var target = e.target;
    if (
        !$(target).is(".dropdown-toggle") &&
        !$(target).parents().is(".dropdown-toggle")
    ) {
      //{ $('.dropdown').hide(); }
      $(".dropdown").slideUp();
    }
  });
});
