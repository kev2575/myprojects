import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

import { useNavigate } from 'react-router-dom';

const CreateItem = (props) => {
  // Define the state with useState hook
  const navigate = useNavigate();
  const [item, setItem] = useState({
    name: '',
    description: '',
    img: '',
  });

  const onChange = (e) => {
    setItem({ ...item, [e.target.name]: e.target.value });
  };

  const onSubmit = (e) => {
    e.preventDefault();

    axios
      .post('http://localhost:8082/api/items', item)
      .then((res) => {
        setItem({
          name: '',
          description: '',
          img: '',
        });

        // Push to /
        navigate('/');
        navigate('/SecondPage')
      })
      .catch((err) => {
        console.log('Error in CreateItem!');
      });
  };
  return(
  <div className='CreateItem'>
    <div className = 'Menu'>
      <Link to='/SecondPage' className='menu-button'>
        Back
      </Link>
    </div>
    <div className = 'Form'>
      <h2>Create Item</h2>
        <form onSubmit={onSubmit}>
              <div className='form-group'>
                <b>Name</b>
                <br />
                <input
                  type='text'
                  placeholder='Sandra'
                  name='name'
                  className='form-control'
                  value={item.name}
                  onChange={onChange}
                />
              </div>
              <div className='form-group'>
                <b>Description</b>
                <br />
                <input
                  type='text'
                  placeholder='Loveable rogue with a bad mouth'
                  name='description'
                  className='form-control long-text'
                  value={item.description}
                  onChange={onChange}
                />
              </div>
              <div className='form-group'>
                <b>Image</b>
                <br />
                <input
                  type='text'
                  placeholder='smiley.jfif'
                  name='img'
                  className='form-control'
                  value={item.img}
                  onChange={onChange}
                />
              </div>

              <input
                type='submit'
                className='submit'
              />
            </form>
    </div>
  </div>
  )}
  export default CreateItem;