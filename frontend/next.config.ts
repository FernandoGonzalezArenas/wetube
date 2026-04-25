import type { NextConfig } from "next";

const nextConfig: NextConfig = {
images: {
dangerouslyAllowSVG: true,
  remotePatterns: [
    {
      protocol: 'http',
      hostname: 'localhost',
      port: '8080',
      pathname: '/storage/**',
    },
    //si se decide obtener las imagenes directo de minio
    {
      protocol: 'http',
      hostname: 'localhost',
      port: '9000',
      pathname: '/**',
    },
  ],
  unoptimized: true,
},
};

export default nextConfig;
