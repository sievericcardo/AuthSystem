FROM ubuntu:latest

RUN <<EOF
    apt-get update
    apt-get install -y openjdk-17-jdk
EOF

# Add the application's jar to the container
COPY build/libs/*.jar /auth/auth_tool.jar

WORKDIR /auth

# Run the jar file
ENTRYPOINT ["java","-jar","auth_tool.jar"]

EXPOSE 8081