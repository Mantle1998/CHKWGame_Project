      // 使用 fetch API 來加載 ./html/header.html
      fetch('./html/header.html')
          .then(response => response.text())
          .then(data => {
              document.getElementById('header-container').innerHTML = data;
          })
          .catch(error => console.error('Error loading header:', error));

  // 使用 fetch API 來加載 ./html/footer.html
      fetch('./html/footer.html')
          .then(response => response.text())
          .then(data => {
              document.getElementById('footer-container').innerHTML = data;
          })
          .catch(error => console.error('Error loading footer:', error));

   // 使用 fetch API 來加載 ./html/saheader.html
   fetch('./html/saheader.html')
   .then(response => response.text())
   .then(data => {
       document.getElementById('saheader-container').innerHTML = data;
   })
   .catch(error => console.error('Error loading footer:', error));         
