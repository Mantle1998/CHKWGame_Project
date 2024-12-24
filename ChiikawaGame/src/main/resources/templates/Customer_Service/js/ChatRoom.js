        
        const chat = document.getElementById('chat');
        const messageInput = document.getElementById('message');
        const sendButton = document.getElementById('sendButton');

        // 建立 WebSocket 連接
        const socket = new WebSocket('ws://localhost:3000');

        // 當接收到訊息時顯示在聊天區域
        socket.addEventListener('message', (event) => {
            const msg = document.createElement('div');
            msg.textContent = event.data;
            chat.appendChild(msg);
            chat.scrollTop = chat.scrollHeight; // 滾動到最新訊息
        });

        // 當點擊發送按鈕時
        sendButton.addEventListener('click', () => {
            const message = messageInput.value;
            if (message) {
                socket.send(message); // 發送訊息
                messageInput.value = ''; // 清空輸入框
            }
        });

        // 當按下 Enter 鍵時也能發送訊息
        messageInput.addEventListener('keypress', (event) => {
            if (event.key === 'Enter') {
                sendButton.click();
            }
        });

        