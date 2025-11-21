import { Layout, Menu } from "antd";
import { Outlet, Link } from "react-router-dom";
import {
  DashboardOutlined,
  UserOutlined,
  ShopOutlined,
  AppstoreOutlined,
  CloudOutlined,
  SendOutlined,
} from "@ant-design/icons";

const { Header, Sider, Content } = Layout;

export default function AdminLayout() {
  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sider theme="dark">
        <Menu theme="dark" mode="inline">
          <Menu.Item key="1" icon={<DashboardOutlined />}>
            <Link to="/admin">Dashboard</Link>
          </Menu.Item>

          <Menu.Item key="2" icon={<UserOutlined />}>
            <Link to="/admin/users">Users</Link>
          </Menu.Item>

          <Menu.Item key="3" icon={<ShopOutlined />}>
            <Link to="/admin/restaurants">Restaurants</Link>
          </Menu.Item>

          <Menu.Item key="4" icon={<AppstoreOutlined />}>
            <Link to="/admin/orders">Orders</Link>
          </Menu.Item>

          <Menu.Item key="5" icon={<CloudOutlined />}>
            <Link to="/admin/drones">Drones</Link>
          </Menu.Item>

          <Menu.Item key="6" icon={<SendOutlined />}>
            <Link to="/admin/tasks">Delivery Tasks</Link>
          </Menu.Item>
        </Menu>
      </Sider>

      <Layout>
        <Header style={{ color: "white" }}>ADMIN PANEL</Header>
        <Content style={{ padding: 24 }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}
