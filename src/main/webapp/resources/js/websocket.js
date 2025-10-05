/**
 * WebSocket клиент для real-time обновлений
 */
class LabWorkWebSocketClient {
    constructor() {
        this.socket = null;
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 5;
        this.reconnectDelay = 1000;
        this.init();
    }
    
    init() {
        this.connect();
    }
    
    connect() {
        try {
            const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
            const wsUrl = `${protocol}//${window.location.host}${window.location.pathname.replace('/index.xhtml', '')}/labwork-updates`;
            
            this.socket = new WebSocket(wsUrl);
            
            this.socket.onopen = (event) => {
                console.log('WebSocket соединение установлено');
                this.reconnectAttempts = 0;
                this.showNotification('Соединение с сервером установлено', 'success');
            };
            
            this.socket.onmessage = (event) => {
                this.handleMessage(event.data);
            };
            
            this.socket.onclose = (event) => {
                console.log('WebSocket соединение закрыто');
                this.attemptReconnect();
            };
            
            this.socket.onerror = (error) => {
                console.error('WebSocket ошибка:', error);
                this.showNotification('Ошибка соединения с сервером', 'error');
            };
            
        } catch (error) {
            console.error('Ошибка создания WebSocket соединения:', error);
            this.attemptReconnect();
        }
    }
    
    handleMessage(data) {
        try {
            const message = JSON.parse(data);
            
            switch (message.type) {
                case 'created':
                    this.showNotification(message.message, 'success');
                    this.refreshData();
                    break;
                case 'updated':
                    this.showNotification(message.message, 'info');
                    this.refreshData();
                    break;
                case 'deleted':
                    this.showNotification(message.message, 'warning');
                    this.refreshData();
                    break;
                case 'special':
                    this.showNotification(message.message, 'info');
                    this.refreshData();
                    break;
                default:
                    console.log('Получено сообщение:', message);
            }
        } catch (error) {
            console.error('Ошибка обработки сообщения:', error);
        }
    }
    
    attemptReconnect() {
        if (this.reconnectAttempts < this.maxReconnectAttempts) {
            this.reconnectAttempts++;
            console.log(`Попытка переподключения ${this.reconnectAttempts}/${this.maxReconnectAttempts}`);
            
            setTimeout(() => {
                this.connect();
            }, this.reconnectDelay * this.reconnectAttempts);
        } else {
            console.error('Превышено максимальное количество попыток переподключения');
            this.showNotification('Не удалось подключиться к серверу', 'error');
        }
    }
    
    showNotification(message, type) {
        // Создаем уведомление
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;
        
        // Добавляем стили
        notification.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 20px;
            border-radius: 4px;
            color: white;
            font-weight: bold;
            z-index: 10000;
            max-width: 300px;
            word-wrap: break-word;
            box-shadow: 0 2px 10px rgba(0,0,0,0.3);
            animation: slideIn 0.3s ease-out;
        `;
        
        // Устанавливаем цвет в зависимости от типа
        switch (type) {
            case 'success':
                notification.style.backgroundColor = '#28a745';
                break;
            case 'error':
                notification.style.backgroundColor = '#dc3545';
                break;
            case 'warning':
                notification.style.backgroundColor = '#ffc107';
                notification.style.color = '#212529';
                break;
            case 'info':
                notification.style.backgroundColor = '#17a2b8';
                break;
            default:
                notification.style.backgroundColor = '#6c757d';
        }
        
        // Добавляем анимацию
        const style = document.createElement('style');
        style.textContent = `
            @keyframes slideIn {
                from { transform: translateX(100%); opacity: 0; }
                to { transform: translateX(0); opacity: 1; }
            }
            @keyframes slideOut {
                from { transform: translateX(0); opacity: 1; }
                to { transform: translateX(100%); opacity: 0; }
            }
        `;
        document.head.appendChild(style);
        
        // Добавляем уведомление на страницу
        document.body.appendChild(notification);
        
        // Автоматически удаляем через 5 секунд
        setTimeout(() => {
            notification.style.animation = 'slideOut 0.3s ease-in';
            setTimeout(() => {
                if (notification.parentNode) {
                    notification.parentNode.removeChild(notification);
                }
            }, 300);
        }, 5000);
    }
    
    refreshData() {
        // Обновляем страницу, если это страница со списком лабораторных работ
        if (window.location.pathname.includes('labworks') || window.location.pathname.includes('index')) {
            // Используем JSF для обновления данных
            const refreshButton = document.querySelector('[id$="refreshButton"]');
            if (refreshButton) {
                refreshButton.click();
            } else {
                // Если кнопки обновления нет, перезагружаем страницу
                setTimeout(() => {
                    window.location.reload();
                }, 1000);
            }
        }
    }
    
    sendMessage(message) {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(message);
        } else {
            console.warn('WebSocket не подключен');
        }
    }
    
    disconnect() {
        if (this.socket) {
            this.socket.close();
            this.socket = null;
        }
    }
}

// Инициализируем WebSocket клиент при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    window.labWorkWebSocket = new LabWorkWebSocketClient();
});

// Очищаем соединение при закрытии страницы
window.addEventListener('beforeunload', function() {
    if (window.labWorkWebSocket) {
        window.labWorkWebSocket.disconnect();
    }
});
