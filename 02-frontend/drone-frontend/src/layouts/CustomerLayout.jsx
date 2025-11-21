import { Layout } from "antd";
import { Outlet } from "react-router-dom";

const { Header, Content } = Layout;

export default function CustomerLayout() {
  return (
    <Layout>
      <Header style={{ color: "white" }}>FOOD DELIVERY</Header>
      <Content style={{ padding: 24 }}>
        <Outlet />
      </Content>
    </Layout>
  );
}
