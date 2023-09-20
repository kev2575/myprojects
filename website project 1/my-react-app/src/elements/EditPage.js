import React, { useState, useEffect } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

function EditPage(props) {
  const [item, setItem] = useState({
    name: '',
    description: '',
    img: '',
  });

  const { id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get(`http://localhost:8082/api/items/${id}`)
      .then((res) => {
        setItem({
          name: res.data.name,
          description: res.data.description,
          img: res.data.img,
        });
      })
      .catch((err) => {
        console.log('Error from EditPage');
      });
  }, [id]);

  const onChange = (e) => {
    setItem({ ...item, [e.target.name]: e.target.value });
  };

  const onSubmit = (e) => {
    e.preventDefault();

    const data = {
      name: item.name,
      description: item.description,
      img: item.img,
    };

    axios
      .put(`http://localhost:8082/api/items/${id}`, data)
      .then((res) => {
        navigate(`/SecondPage`);
      })
      .catch((err) => {
        console.log('Error in EditPage!');
      });
  };

  return (
    <div className = 'EditPage'>
      <div className = 'Menu'>
        <Link to='/SecondPage' className='menu-button'>
          Back
        </Link>
      </div>
      <div className = 'Form'>
        <h2>Edit Item</h2>
      <form noValidate onSubmit={onSubmit}>
            <div className='form-group'>
              <b>Name</b>
              <br />
              <input
                type='text'
                placeholder='Name of the Item'
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
                placeholder='Description of the Item'
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
                placeholder='Item image'
                name='img'
                className='form-control long-text'
                value={item.img}
                onChange={onChange}
              />
            </div>
            <br />
            <button
              type='submit'
              className='submit'
            >
              Update Item
            </button>
          </form>
      </div>
    </div>
  );
}

export default EditPage;
