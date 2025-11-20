import { Outlet, Link } from "react-router-dom";
import {
  Drawer,
  List,
  ListItemButton,
  ListItemText,
  Box,
  Toolbar,
  AppBar,
  Typography,
} from "@mui/material";

export default function AdminLayout() {
  return (
    <Box sx={{ display: "flex" }}>
      <AppBar position="fixed">
        <Toolbar>
          <Typography variant="h6">Admin Panel</Typography>
        </Toolbar>
      </AppBar>

      <Drawer variant="permanent" sx={{ width: 240 }}>
        <Toolbar />
        <List>
          <ListItemButton component={Link} to="/admin">
            <ListItemText primary="Dashboard" />
          </ListItemButton>
          <ListItemButton component={Link} to="/admin/drones">
            <ListItemText primary="Drones" />
          </ListItemButton>
          <ListItemButton component={Link} to="/admin/orders">
            <ListItemText primary="Orders" />
          </ListItemButton>
        </List>
      </Drawer>

      <Box sx={{ flexGrow: 1, p: 3, ml: 30 }}>
        <Toolbar />
        <Outlet />
      </Box>
    </Box>
  );
}
