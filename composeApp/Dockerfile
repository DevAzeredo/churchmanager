# Use a imagem oficial do Nginx como base
FROM nginx:alpine

# Copie os arquivos estáticos para o diretório de documentos do Nginx
COPY ./build/dist/wasmJs/productionExecutable/ /usr/share/nginx/html

# Exponha a porta padrão do Nginx
EXPOSE 80

# Inicie o Nginx
CMD ["nginx", "-g", "daemon off;"]

