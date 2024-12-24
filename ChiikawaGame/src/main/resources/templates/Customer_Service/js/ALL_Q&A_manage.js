function loadData() {
    fetch("http://localhost:8081/HibernateWebProject/CSQADataServlet")
        .then(response => {
            if (!response.ok) {
                throw new Error('網路錯誤，請稍後再試。');
            }
            return response.json();
        })
        .then(data => {
            const tbody = document.querySelector("#data-table tbody");
            tbody.innerHTML = ""; // 清空現有的資料

            data.forEach(item => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td><input type="text" value="${item.CSQADataSort}" id="sort-${item.CSQADataid}"></td>
                    <td><input type="text" value="${item.CSQADataTitle}" id="title-${item.CSQADataid}"></td>
                    <td><textarea id="content-${item.CSQADataid}">${item.CSQADataContent}</textarea></td>
                    <td>
                        <select id="status-${item.CSQADataid}">
                            <option value="上架" ${item.CSQADataChack === 1 ? 'selected' : ''}>上架</option>
                            <option value="下架" ${item.CSQADataChack === 0 ? 'selected' : ''}>下架</option>
                        </select>
                    </td>
                    <td>
                        <button onclick="saveChanges(${item.CSQADataid})">儲存</button>
                        <button onclick="deleteItem(${item.CSQADataid})">刪除</button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('發生錯誤:', error);
            document.getElementById('modalMessage').innerText = '加載失敗: ' + error.message;
            $('#successModal').modal('show');
        });
}

// 保存更改的函數
function saveChanges(id) {
    const sort = document.getElementById(`sort-${id}`).value;
    const title = document.getElementById(`title-${id}`).value;
    const content = document.getElementById(`content-${id}`).value;
    const status = document.getElementById(`status-${id}`).value === "上架" ? 1 : 0;

    fetch("http://localhost:8081/HibernateWebProject/CSQADataServlet", {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ CSQADataSort: sort, CSQADataTitle: title, CSQADataContent: content, CSQADataChack: status, id: id })
    }).then(response => {
        if (!response.ok) {
            throw new Error('網路錯誤，請稍後再試。');
        }
        return response.json();
    }).then(data => {
        document.getElementById('modalMessage').innerText = data.message;
        $('#successModal').modal('show');
        loadData(); // 重新加載資料
    }).catch(error => {
        console.error('發生錯誤:', error);
        document.getElementById('modalMessage').innerText = '保存失敗: ' + error.message;
        $('#successModal').modal('show');
    });
}

// 刪除項目的函數
function deleteItem(id) {
    fetch(`http://localhost:8081/HibernateWebProject/CSQADataServlet?id=${id}`, {
        method: 'DELETE'
    }).then(response => {
        if (!response.ok) {
            throw new Error('網路錯誤，請稍後再試。');
        }
        return response.json();
    }).then(data => {
        document.getElementById('modalMessage').innerText = data.message;
        $('#successModal').modal('show');
        loadData(); // 重新加載資料
    }).catch(error => {
        console.error('發生錯誤:', error);
        document.getElementById('modalMessage').innerText = '刪除失敗: ' + error.message;
        $('#successModal').modal('show');
    });
}

// 在DOM加載完成後調用loadData
document.addEventListener("DOMContentLoaded", loadData);
