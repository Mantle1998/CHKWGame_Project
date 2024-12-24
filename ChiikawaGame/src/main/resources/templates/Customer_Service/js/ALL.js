// 右側內容的展開/收納功能
document.querySelectorAll('.right-content ul li button').forEach(button => {
    button.addEventListener('click', () => {
        const content = button.nextElementSibling;
        content.style.display = content.style.display === 'none' || content.style.display === '' ? 'block' : 'none';
    });
});

// 當表單提交時執行
document.getElementById('contactForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 阻止表單的默認提交行為
    document.getElementById('thankYouMessage').style.display = 'block'; // 顯示感謝消息
    this.reset(); // 重置表單
});