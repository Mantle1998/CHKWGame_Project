// 初始化頁面時載入表單資料
document.addEventListener("DOMContentLoaded", loadCSfrom);

// 查詢表單資料
function searchCSfrom() {
    const filter = document.getElementById("searchInput").value.toLowerCase();
    const rows = document.querySelectorAll("#forumTablebody tr");
    
    rows.forEach(row => {
        const CSFormTitle = row.cells[2].textContent.toLowerCase();  // 取得標題
        const CSFormId = row.cells[0].textContent.toLowerCase();  // 取得表單ID
        
        // 根據表單ID或標題過濾資料
        const match = CSFormTitle.includes(filter) || CSFormId.includes(filter);
        row.style.display = match ? "" : "none";  // 顯示或隱藏符合條件的行
    });
}

// 載入表單資料
function loadCSfrom() {
    fetch("/form_manage/json")  // 假設這是您後端的 API 端點
        .then(response => response.json())
        .then(data => {
            const tableBody = document.querySelector("#forumTable tbody");
            tableBody.innerHTML = "";  // 清空現有的表格內容

            data.forEach(form => {
                const row = `
                    <tr>
                        <td>${form.CSFormId}</td>
                        <td>${form.CSFormSort}</td>
                        <td>${form.CSFormTitle}</td>
                        <td>${form.CSFormContent}</td>
                        <td>${form.CSFormDate}</td>
                        <td>${form.CSformChack === 0 ? '未審核' : '已發布'}</td>
                    </tr>
                `;
                tableBody.insertAdjacentHTML("beforeend", row);  // 將新行加入表格
            });
        })
        .catch(error => {
            console.error("載入表單資料錯誤：", error);
            alert("無法載入表單資料，請稍後再試。");
        });
}
