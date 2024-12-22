import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';
import { API_LOGGED_IN_USER_DETAILS } from '../../env';

const userSlice = createSlice({
  name: 'user',
  initialState: {
    fullName: '',
    firstName: '',
    lastName: '',
    gender: '',
    role: '',
    email: '',
    dob: '',
    loading: false,
    error: null,
  },


  reducers: {
    fetchUserStart(state) {
      state.loading = true;
    },
    fetchUserSuccess(state, action) {
      state.fullName = action.payload.fullName;
      state.gender = action.payload.gender;
      state.role = action.payload.role;
      state.email = action.payload.email;
      state.firstName = action.payload.firstName;
      state.lastName = action.payload.lastName;
      state.dob = action.payload.dob;
      state.loading = false;
    },
    fetchUserFailure(state, action) {
      state.error = action.payload;
      state.loading = false;
    },
  },
});

export const { fetchUserStart, fetchUserSuccess, fetchUserFailure } = userSlice.actions;

export const fetchLoggedInUserDetails = (userId) => async (dispatch) => {
  dispatch(fetchUserStart());
  try {
    const response = await axios.get(`${API_LOGGED_IN_USER_DETAILS}/${userId}`);
    const { firstName, lastName, gender,role,email,dob } = response.data;
    const fullName = `${firstName} ${lastName}`;
    dispatch(fetchUserSuccess({  firstName, lastName,fullName, gender,role,email,dob }));
  } catch (error) {
    dispatch(fetchUserFailure('Error loading data from the server'));
    console.error('Error fetching logged in user details:', error);
  }
};

export default userSlice.reducer;
