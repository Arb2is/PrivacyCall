const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 8787 });
const rooms = {};

console.log('Signaling server started on ws://localhost:8787');

wss.on('connection', (ws, req) => {
    const url = new URL(req.url, `ws://${req.headers.host}`);
    const sid = url.searchParams.get('sid');

    if (!sid) {
        ws.close(1008, 'Session ID (sid) is required');
        return;
    }

    if (!rooms[sid]) {
        rooms[sid] = [];
    }

    // Limit room to 2 participants
    if (rooms[sid].length >= 2) {
        ws.close(1013, 'Room is full');
        return;
    }

    ws.sid = sid;
    rooms[sid].push(ws);
    console.log(`Client joined room ${sid}. Total clients: ${rooms[sid].length}`);

    // Room TTL
    setTimeout(() => {
        if (rooms[sid]) {
            console.log(`Closing room ${sid} due to TTL`);
            rooms[sid].forEach(client => client.close(1000, 'Room expired'));
            delete rooms[sid];
        }
    }, 120 * 1000);

    ws.on('message', (message) => {
        // Broadcast to the other client in the room
        rooms[ws.sid].forEach(client => {
            if (client !== ws && client.readyState === WebSocket.OPEN) {
                client.send(message);
            }
        });
    });

    ws.on('close', () => {
        if (rooms[ws.sid]) {
            rooms[ws.sid] = rooms[ws.sid].filter(client => client !== ws);
            if (rooms[ws.sid].length === 0) {
                console.log(`Room ${ws.sid} is empty, deleting.`);
                delete rooms[ws.sid];
            }
        }
    });
});
