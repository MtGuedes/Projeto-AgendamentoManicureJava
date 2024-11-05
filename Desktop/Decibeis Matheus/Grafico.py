import serial
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import serial.tools.list_ports

ports = serial.tools.list_ports.comports()
for port in ports:
    print(port.device)

# Configuração da porta serial (ajuste a porta conforme seu sistema)
# Exemplo: no Windows, pode ser 'COM3', 'COM4', etc. No Linux/Mac, algo como '/dev/ttyUSB0'
porta_serial = 'COM3'  # Porta correta
baud_rate = 9600  # Mesmo baud rate definido no Arduino

# Conexão com o Arduino
ser = serial.Serial(porta_serial, baud_rate)

# Lista para armazenar os valores de dB
valores_dB = []

# Função de inicialização do gráfico
fig, ax = plt.subplots()
linha, = ax.plot([], [], lw=2)
ax.set_ylim(0, 100)  # Faixa de valores do gráfico (ajuste conforme necessário)
ax.set_xlim(0, 100)  # Mostra os últimos 100 valores de dB
ax.set_xlabel('Tempo')
ax.set_ylabel('Decibéis (dB)')

# Função de atualização do gráfico
def atualizar_grafico(frame):
    global valores_dB

    try:
        # Leitura da linha recebida via serial
        linha_serial = ser.readline().decode('utf-8').strip()

        # Verifica se a linha contém o valor de dB
        if "Intensidade" in linha_serial:
            # Extração do valor de dB
            valor_dB = int(linha_serial.split()[1])

            # Adiciona o valor à lista
            valores_dB.append(valor_dB)

            # Mantém apenas os últimos 100 valores para plotar
            if len(valores_dB) > 100:
                valores_dB = valores_dB[-100:]

            # Atualiza os dados do gráfico
            linha.set_data(range(len(valores_dB)), valores_dB)

            # Ajusta o eixo x para mostrar apenas os últimos 100 valores
            ax.set_xlim(0, len(valores_dB))

    except Exception as e:
        print(f"Erro: {e}")

    return linha,

# Animação do gráfico
ani = animation.FuncAnimation(fig, atualizar_grafico, frames=100, interval=100, blit=True)

plt.show()

# Fechar a conexão serial quando o programa terminar
ser.close()
