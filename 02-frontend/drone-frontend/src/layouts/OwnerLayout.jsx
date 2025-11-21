import { Layout, Menu } from "antd";
import { Outlet, Link } from "react-router-dom";
import { ShopOutlined, AppstoreOutlined } from "@ant-design/icons";

const { Header, Sider, Content } = Layout;

export default function OwnerLayout() {
  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sider theme="dark">
        <Menu theme="dark" mode="inline">
          <Menu.Item key="1" icon={<ShopOutlined />}>
            <Link to="/owner">Restaurant Dashboard</Link>
          </Menu.Item>

          <Menu.Item key="2" icon={<AppstoreOutlined />}>
            <Link to="/owner/menu">Menu Items</Link>
          </Menu.Item>
        </Menu>
      </Sider>

      <Layout>
        <Header style={{ color: "white" }}>RESTAURANT OWNER PANEL</Header>
        <Content style={{ padding: 24 }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}
