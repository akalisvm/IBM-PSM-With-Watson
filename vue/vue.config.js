// CORS Configuration
module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://82.157.202.100:9090',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}