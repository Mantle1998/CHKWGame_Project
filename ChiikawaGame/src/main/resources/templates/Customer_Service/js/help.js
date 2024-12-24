$(document).ready(function() {
  $('#contactForm').on('submit', function(e) {
      e.preventDefault();

      const category = $('#questionCategory').val();
      const title = $('#questionTitle').val();
      const content = $('#questionContent').val();

      if (!category || !title || !content) {
          alert("請填寫所有欄位！");
          return;
      }

      $.post("http://localhost:8081/tw/customer_service/CS_form/CSFormServlet", {
          CSFormSort: category, // 改成和 Servlet 中匹配的名稱
          CSFormTitle: title,
          CSFormContent: content,
          action: 'contact'
      })
      .done(function(data) {
          alert("提交成功！");
          $('#contactForm')[0].reset();
          $('#thankYouMessage').show(); // 顯示感謝訊息
      })
      .fail(function(xhr, status, error) {
          alert("提交失敗: " + error);
      });
  });
});
