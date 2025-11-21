import { useContext } from "react";
import { Form, Input, Button, Card, message } from "antd";
import AuthContext from "../../context/AuthContext";

export default function Login() {
  const { login } = useContext(AuthContext);

  const onFinish = async (values) => {
    try {
      const res = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(values),
      });

      if (!res.ok) {
        return message.error("Login failed. Check email/password!");
      }

      const data = await res.json();

      if (!data.token) {
        return message.error("Server did not return token!");
      }

      login(data.token);
      window.location.href = "/admin"; // nếu bạn đang test admin
    } catch (err) {
      message.error("Cannot connect to API Gateway" + err.message);
    }
  };

  return (
    <div style={{ marginTop: 100, display: "flex", justifyContent: "center" }}>
      <Card title="Login" style={{ width: 400 }}>
        <Form onFinish={onFinish}>
          <Form.Item name="email" rules={[{ required: true }]}>
            <Input placeholder="Email" />
          </Form.Item>

          <Form.Item name="password" rules={[{ required: true }]}>
            <Input.Password placeholder="Password" />
          </Form.Item>

          <Button type="primary" htmlType="submit" block>
            Login
          </Button>
        </Form>
      </Card>
    </div>
  );
}
