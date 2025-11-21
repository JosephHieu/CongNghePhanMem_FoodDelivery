import {
  Table,
  Button,
  Space,
  Modal,
  Form,
  Input,
  message,
  Upload,
  Tag,
} from "antd";
import { useEffect, useState, useCallback } from "react";
import { UploadOutlined } from "@ant-design/icons";
import api from "../../../utils/api";

export default function RestaurantList() {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(false);

  const [isAddOpen, setIsAddOpen] = useState(false);
  const [isEditOpen, setIsEditOpen] = useState(false);
  const [isEditImageOpen, setIsEditImageOpen] = useState(false);

  const [file, setFile] = useState(null); // for add
  const [editImageFile, setEditImageFile] = useState(null); // for edit image

  const [editingRestaurant, setEditingRestaurant] = useState(null);

  const [form] = Form.useForm();
  const [editForm] = Form.useForm();

  // ============================================
  // LOAD RESTAURANTS
  // ============================================
  const loadRestaurants = useCallback(async () => {
    try {
      setLoading(true);
      const res = await api.get("/api/restaurants");
      setRestaurants(res.data);
    } catch (err) {
      console.error(err);
      message.error("Failed to load restaurants: " + err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadRestaurants();
  }, [loadRestaurants]);

  // ============================================
  // CREATE RESTAURANT (STEP 1: create, STEP 2: upload image)
  // ============================================
  const handleAdd = async () => {
    try {
      const values = await form.validateFields();

      // 1. Tạo restaurant (không ảnh)
      const res = await api.post("/api/restaurants", values);
      const created = res.data; // có _id

      // 2. Nếu có ảnh thì upload
      if (file) {
        const fd = new FormData();
        fd.append("file", file);

        await api.post(`/api/restaurants/${created._id}/upload-image`, fd, {
          headers: { "Content-Type": "multipart/form-data" },
        });
      }

      message.success("Restaurant created!");
      form.resetFields();
      setFile(null);
      setIsAddOpen(false);
      loadRestaurants();
    } catch (err) {
      console.error(err);
      message.error("Failed to create restaurant");
    }
  };

  // ============================================
  // EDIT RESTAURANT INFO (NO IMAGE)
  // ============================================
  const handleEdit = async () => {
    try {
      const values = await editForm.validateFields();

      await api.put(`/api/restaurants/${editingRestaurant._id}`, values);

      message.success("Restaurant updated!");
      setIsEditOpen(false);
      setEditingRestaurant(null);
      loadRestaurants();
    } catch (err) {
      console.error(err);
      message.error("Failed to update restaurant");
    }
  };

  // ============================================
  // EDIT RESTAURANT IMAGE ONLY
  // ============================================
  const handleEditImage = async () => {
    if (!editImageFile) {
      message.error("Please choose an image");
      return;
    }

    try {
      const fd = new FormData();
      fd.append("file", editImageFile);

      await api.post(
        `/api/restaurants/${editingRestaurant._id}/upload-image`,
        fd,
        { headers: { "Content-Type": "multipart/form-data" } }
      );

      message.success("Image updated!");
      setIsEditImageOpen(false);
      setEditImageFile(null);
      setEditingRestaurant(null);
      loadRestaurants();
    } catch (err) {
      console.error(err);
      message.error("Failed to update image");
    }
  };

  // ============================================
  // BAN / UNBAN RESTAURANT
  // ============================================
  const toggleBan = async (id, status) => {
    try {
      await api.put(`/api/restaurants/${id}/status`, { status });
      message.success("Updated status");
      loadRestaurants();
    } catch (err) {
      console.error(err);
      message.error("Failed: " + err.message);
    }
  };

  const columns = [
    { title: "Name", dataIndex: "name" },
    { title: "Address", dataIndex: "address" },
    {
      title: "Image",
      dataIndex: "imageUrl",
      render: (url) =>
        url ? (
          <img src={url} style={{ width: 60, borderRadius: 6 }} />
        ) : (
          <Tag>No Image</Tag>
        ),
    },
    {
      title: "Status",
      dataIndex: "status",
      render: (s) =>
        s === 1 ? (
          <Tag color="green">ACTIVE</Tag>
        ) : (
          <Tag color="red">BANNED</Tag>
        ),
    },
    {
      title: "Actions",
      render: (_, r) => (
        <Space>
          {/* Edit Info */}
          <Button
            onClick={() => {
              setEditingRestaurant(r);
              editForm.setFieldsValue(r);
              setIsEditOpen(true);
            }}
          >
            Edit
          </Button>

          {/* Edit Image */}
          <Button
            onClick={() => {
              setEditingRestaurant(r);
              setIsEditImageOpen(true);
            }}
          >
            Edit Image
          </Button>

          {/* Ban / Unban */}
          <Button
            danger={r.status === 1}
            type={r.status === 0 ? "primary" : "default"}
            onClick={() => toggleBan(r._id, r.status === 1 ? 0 : 1)}
          >
            {r.status === 1 ? "Ban" : "Unban"}
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <h1>Restaurant Management</h1>

      <Button type="primary" onClick={() => setIsAddOpen(true)}>
        Add Restaurant
      </Button>

      <Table
        columns={columns}
        dataSource={restaurants}
        rowKey="_id"
        loading={loading}
        style={{ marginTop: 20 }}
      />

      {/* ================= Add Restaurant Modal ================= */}
      <Modal
        title="Add Restaurant"
        open={isAddOpen}
        onOk={handleAdd}
        onCancel={() => {
          setIsAddOpen(false);
          form.resetFields();
          setFile(null);
        }}
      >
        <Form layout="vertical" form={form}>
          <Form.Item
            name="ownerId"
            label="Owner ID"
            rules={[{ required: true }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            name="name"
            label="Restaurant Name"
            rules={[{ required: true }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            name="address"
            label="Address"
            rules={[{ required: true }]}
          >
            <Input />
          </Form.Item>

          <Form.Item name="lat" label="Latitude">
            <Input type="number" />
          </Form.Item>

          <Form.Item name="lng" label="Longitude">
            <Input type="number" />
          </Form.Item>

          <Upload
            beforeUpload={(f) => {
              setFile(f);
              return false; // không upload auto, để mình tự gửi FormData
            }}
            maxCount={1}
          >
            <Button icon={<UploadOutlined />}>Select Image</Button>
          </Upload>
        </Form>
      </Modal>

      {/* ================= Edit Restaurant Modal ================= */}
      <Modal
        title="Edit Restaurant"
        open={isEditOpen}
        onOk={handleEdit}
        onCancel={() => {
          setIsEditOpen(false);
          setEditingRestaurant(null);
        }}
      >
        <Form layout="vertical" form={editForm}>
          <Form.Item name="name" label="Restaurant Name">
            <Input />
          </Form.Item>

          <Form.Item name="address" label="Address">
            <Input />
          </Form.Item>

          <Form.Item name="lat" label="Latitude">
            <Input type="number" />
          </Form.Item>

          <Form.Item name="lng" label="Longitude">
            <Input type="number" />
          </Form.Item>
        </Form>
      </Modal>

      {/* ================= Edit Image Modal ================= */}
      <Modal
        title="Edit Restaurant Image"
        open={isEditImageOpen}
        onOk={handleEditImage}
        onCancel={() => {
          setIsEditImageOpen(false);
          setEditImageFile(null);
          setEditingRestaurant(null);
        }}
      >
        <Upload
          beforeUpload={(f) => {
            setEditImageFile(f);
            return false;
          }}
          maxCount={1}
        >
          <Button icon={<UploadOutlined />}>Select New Image</Button>
        </Upload>
      </Modal>
    </div>
  );
}
